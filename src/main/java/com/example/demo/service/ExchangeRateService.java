package com.example.demo.service;

import com.example.demo.dto.SourceDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();

    private Map<String, BigDecimal> rates = new HashMap<>();

    private long lastFetchTime = 0;
    private static final long CACHE_DURATION = 10 * 60 * 1000; // 10 min

    private static final int INTERNAL_SCALE = 8;
    private static final int FINAL_SCALE = 2;

    private synchronized void refreshRatesIfNeeded(String base) {
        long now = System.currentTimeMillis();

        if (now - lastFetchTime < CACHE_DURATION && !rates.isEmpty()) {
            return;
        }

        String url = "https://open.er-api.com/v6/latest/" + base;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !"success".equals(response.get("result"))) {
            if (!rates.isEmpty()) return; // fallback to old rates
            throw new RuntimeException("Failed to fetch exchange rates");
        }

        Map<String, Double> fetchedRates = (Map<String, Double>) response.get("rates");

        Map<String, BigDecimal> newRates = new HashMap<>();
        fetchedRates.put(base, 1.0d);
        for (Map.Entry<String, Double> entry : fetchedRates.entrySet()) {
            newRates.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
        }

        newRates.put(base, BigDecimal.ONE); // ensure base exists

        this.rates = Collections.unmodifiableMap(newRates);
        this.lastFetchTime = now;
    }

    private BigDecimal convert(BigDecimal amount, String from, String to) {
        if (from.equals(to)) return amount;

        BigDecimal fromRate = rates.get(from);
        BigDecimal toRate = rates.get(to);

        if (fromRate == null || toRate == null) {
            throw new RuntimeException("Unsupported currency: " + from + " or " + to);
        }

        BigDecimal amountInBase = amount.divide(fromRate, INTERNAL_SCALE, RoundingMode.HALF_UP);

        return amountInBase
                .multiply(toRate)
                .setScale(FINAL_SCALE, RoundingMode.HALF_UP);
    }

    public List<SourceDTO> convertSources(List<SourceDTO> sources, String targetCurrency) {
        refreshRatesIfNeeded(targetCurrency);

        return sources.stream().map(s -> {
            BigDecimal converted = convert(
                    s.getAmount(),
                    s.getCurrency(),
                    targetCurrency
            );

            SourceDTO dto = SourceDTO
                    .builder()
                    .id(s.getId())
                    .type(s.getType())
                    .sheetId(s.getSheetId())
                    .createdAt(s.getCreatedAt())
                    .description(s.getDescription())
                    .amount(converted)
                    .currency(targetCurrency)
                    .build();

            return dto;
        }).toList();
    }
}