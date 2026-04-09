package com.example.demo;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Base64;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
public class DemoApplication {
	public static void main(String[] args) {
//		var key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
//		String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
//		System.out.println("token displayed: ");
//		System.out.println(base64);
		SpringApplication.run(DemoApplication.class, args);
	}
}
