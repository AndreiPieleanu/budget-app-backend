This documentation provides useful information about the EuroWise app. 

Architecture: 
1. DB (PostgreSQL - Supabase)
2. Cache (Redis - Upstash Redis)
3. Backend (Java Spring Boot - Render)
4. Frontend (Next.js - Vercel)
5. Email service (Java Spring Boot - SMTP)

Backend
Connect the backend with the DB: 
1. Create supabase project
2. Copy credentials given on dashboard
3. Paste them in application-dev.properties 

Connect the backend with the Redis cache (Docker):
1. Pull redis docker image
2. Create new container with port 6379
3. In the backend add redis properties 

Connect the backend with the Redis cache (Upstash): 
1. Create account on Upstash
2. Create new Redis cache on Upstash
3. Connect it with backend by adding Upstash properties in application-dev.properties

Connect the backend with the frontend: 
1. Configure FRONTEND_URL in application-dev.properties to get the frontend URL you want to test on
2. That's it

Frontend
Connect the frontend with the backend (to test locally): 
1. Change NEXT_PUBLIC_API_URL in .env.local to the corresponding backedn URL. 
2. That's it. 

Connect the frontend with the backend (to test on Vercel): 
1. Change the env variable NEXT_PUBLIC_API_URL to the corresponding backedn URL.
2. That's it.


There are currently 2 stages: dev and prd. 
How to configure for dev:
1. In backend set spring profile in env variable: SPRING_PROFILES_ACTIVE=dev & PORT=8080

How to configure for prd: 
1. Commit & push code on backend.
2. Wait until backend is deployed on Render.
3. Commit & push code on frontend. 
4. Wait until frontend is deployed on Vercel. 