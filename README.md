# Event Management Application (Spring Boot + Angular + JWT)

Full‑stack web application to create, book, and manage events with secure authentication using JWT stored in HttpOnly cookies.  
Back end built with Java/Spring Boot, front end with Angular/TypeScript. The REST API is documented with OpenAPI/Swagger.

## Table of contents
- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick start (full‑stack)](#quick-start-full-stack)
- [Back end configuration](#back-end-configuration)
- [Front end configuration](#front-end-configuration)
- [Authentication and security](#authentication-and-security)
- [API and documentation](#api-and-documentation)
- [Usage examples (cURL)](#usage-examples-curl)
- [Project structure](#project-structure)
- [Build, tests, and run](#build-tests-and-run)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

- Front end: modern, responsive Angular app with reusable components, typed services, and route guards for navigation security.
- Back end: Spring Security + JWT (HttpOnly cookies), user/event/reservation management, and PDF ticket generation.

---

## Features

User:
- Browse and search events
- Secure sign‑up and sign‑in
- Book tickets for events
- Download tickets as PDF
- Manage profile and personal information
- Change password and email

Admin:
- Manage users (roles: `USER`, `ADMIN`)
- Manage events (create, update, delete)
- Manage reservations

Cross‑cutting:
- Secured REST API (Spring Security, JWT via HttpOnly cookie)
- OpenAPI/Swagger documentation
- File storage (uploads) and PDF ticket generation

---

## Architecture

- Back end (Java/Spring Boot)
  - `controller/`: REST controllers (auth, users, events, reservations)
  - `model/`: JPA entities (`User`, `Event`, `Reservation`)
  - `repository/`: Data access (Spring Data JPA)
  - `service/`: Business logic (users, events, reservations, files)
  - `security/`: Security config, JWT, auth filters
  - `dto/`: Data transfer objects
  - `mapper/`: Entity ↔ DTO mappers
  - Storage folders: `tickets/` (generated PDFs), `uploads/` (uploaded files)

- Front end (Angular/TypeScript)
  - Routed pages, reusable components, and services
  - Models (TypeScript interfaces), guards (auth/roles), and interceptors (auth, errors)
  - Responsive navigation and modular structure (clear separation of pages/components/services)

---

## Tech stack

Back end:
- Java 17+
- Spring Boot
- Spring Security (JWT, HttpOnly cookies)
- JPA/Hibernate
- Maven, Lombok
- Swagger/OpenAPI

Front end:
- Angular
- TypeScript
- RxJS
- Angular Router, Forms, HttpClient

---

## Prerequisites

- Java 17 or newer
- Maven (or Maven Wrapper `./mvnw`)
- Node.js LTS + npm
- (Optional) Angular CLI (`npm i -g @angular/cli`)
- A database (PostgreSQL/MySQL/H2)

---

## Quick start (full‑stack)

1) Clone the repository:
```bash
git clone <REPOSITORY_URL>
cd <PROJECT_DIR>
```

2) Back end: configure the database in `src/main/resources/application.properties` (see below), then start:
```bash
./mvnw spring-boot:run
# API available at http://localhost:8081
```

3) Front end: install and run Angular dev server:
```bash
cd frontend
npm ci
# Recommended in dev: run with a proxy to avoid CORS and handle cookies
npm run start
# or: ng serve --proxy-config proxy.conf.json
# App available at http://localhost:4200
```

4) Access:
- Web app: http://localhost:4200
- Swagger UI: http://localhost:8081/swagger-ui.html

---

## Back end configuration

Example `src/main/resources/application.properties`:
```properties
# ========= Server =========
server.port=8081

# ========= Datasource =========
spring.datasource.url=jdbc:postgresql://localhost:5432/events
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# ========= JWT / Security =========
app.jwt.secret=replace_me_with_a_strong_random_secret
app.jwt.expiration=3600000             # 1h (ms)
app.jwt.refresh-expiration=1209600000  # 14d (ms)
app.security.cookie-name=access_token
app.security.cookie-secure=false       # true in production (HTTPS)
app.security.cookie-same-site=Lax      # Lax/Strict/None (None requires HTTPS)

# ========= CORS (if not using Angular proxy) =========
app.cors.allowed-origins=http://localhost:4200
app.cors.allowed-methods=GET,POST,PUT,PATCH,DELETE,OPTIONS
app.cors.allowed-headers=*
app.cors.allow-credentials=true

# ========= File storage =========
app.storage.uploads-dir=uploads
app.storage.tickets-dir=tickets

# ========= Swagger =========
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
```

Notes:
- Use a strong `app.jwt.secret` (32+ chars, random).
- In production, enable HTTPS and set `cookie-secure=true`. If using cross‑site cookies, set `SameSite=None`.

---

## Front end configuration

- Environments (example):
```ts
// frontend/src/environments/environment.ts (development)
export const environment = {
  production: false,
  // Option A (recommended with proxy): use a relative path
  apiBaseUrl: '/api',
  // Option B (no proxy): point directly to the back end
  // apiBaseUrl: 'http://localhost:8081/api',
};
```

```ts
// frontend/src/environments/environment.prod.ts (production)
export const environment = {
  production: true,
  // Prefer a relative path if front and API share the same domain
  apiBaseUrl: '/api',
};
```

- Dev proxy (to avoid CORS and properly forward cookies):
```json
// frontend/proxy.conf.json
{
  "/api": {
    "target": "http://localhost:8081",
    "secure": false,
    "changeOrigin": false,
    "logLevel": "debug"
  }
}
```
Commands:
```bash
# package.json
# "start": "ng serve --proxy-config proxy.conf.json"
npm run start
```

- HTTP interceptor to always send cookies (`withCredentials`):
```ts
// frontend/src/app/interceptors/auth.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const withCreds = req.clone({ withCredentials: true });
    return next.handle(withCreds);
  }
}
```
Register it:
```ts
// frontend/src/app/app.config.ts (Angular standalone) or app.module.ts
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';

export const appConfig = {
  providers: [
    provideHttpClient(withInterceptors([() => new AuthInterceptor()])),
  ]
};
```

- Guards (example):
```ts
// frontend/src/app/guards/auth.guard.ts
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (auth.isAuthenticated()) return true;
  router.navigate(['/login']);
  return false;
};
```

- Downloading PDF tickets:
```ts
this.http.get(`${environment.apiBaseUrl}/reservations/${id}/ticket`, {
  responseType: 'blob',
  withCredentials: true
}).subscribe(blob => saveAs(blob, `ticket-${id}.pdf`));
```

---

## Authentication and security

- Authentication via JWT stored in an `HttpOnly` cookie set by the back end on `POST /api/auth/login`.
- Angular cannot read the cookie (HttpOnly) — this is expected and safer. Use `withCredentials: true` so the browser sends the cookie automatically.
- Typical public routes: `POST /api/auth/register`, `POST /api/auth/login`, Swagger (`/v3/api-docs/**`, `/swagger-ui.html`).
- Protected routes require a valid JWT; use `ROLE_USER`/`ROLE_ADMIN` depending on the endpoint.
- Token refresh if an endpoint like `/api/auth/refresh` exists (implementation dependent).

---

## API and documentation

- Base path: `/api`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

Use Swagger to explore endpoints, DTO schemas, and request/response examples.

---

## Usage examples (cURL)

Register:
```bash
curl -i -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"user@example.com",
    "password":"Password123!",
    "fullName":"Jane Doe"
  }'
```

Login (sets an `HttpOnly` cookie):
```bash
curl -i -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"Password123!"}'
```

Create an event (using cookies with cURL):
```bash
# 1) Login and save cookies
curl -i -c cookies.txt -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"AdminPass123!"}'

# 2) Create event
curl -i -b cookies.txt -X POST http://localhost:8081/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Spring Conference",
    "description":"All about Spring",
    "location":"Paris",
    "startDate":"2025-09-10T09:00:00",
    "endDate":"2025-09-10T17:00:00",
    "capacity":150,
    "price":49.99
  }'
```

Book tickets:
```bash
curl -i -b cookies.txt -X POST http://localhost:8081/api/reservations \
  -H "Content-Type: application/json" \
  -d '{"eventId":1,"quantity":2}'
```

Download a PDF ticket:
```bash
curl -L -b cookies.txt -o ticket-123.pdf \
  http://localhost:8081/api/reservations/123/ticket
```

Payloads and paths may vary — see Swagger for the authoritative reference.

---

## Project structure

```text
.
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ com/
│     │     └─ manu/
│     │        └─ template/
│     │           ├─ controller/
│     │           ├─ dto/
│     │           ├─ mapper/
│     │           ├─ model/
│     │           ├─ repository/
│     │           ├─ security/
│     │           └─ service/
│     └─ resources/
│        └─ application.properties
│
├─ frontend/
│  ├─ src/
│  │  ├─ app/
│  │  │  ├─ pages/           # routed pages (events, details, login, profile, admin, etc.)
│  │  │  ├─ components/      # reusable components (cards, forms, layout…)
│  │  │  ├─ services/        # services (auth, events, reservations, user)
│  │  │  ├─ models/          # TS interfaces (User, Event, Reservation…)
│  │  │  ├─ guards/          # guards (auth, role)
│  │  │  ├─ interceptors/    # interceptors (auth withCredentials, errors)
│  │  │  └─ shared/          # shared modules/utilities
│  │  ├─ assets/
│  │  └─ environments/
│  │     ├─ environment.ts
│  │     └─ environment.prod.ts
│  ├─ proxy.conf.json
│  ├─ package.json
│  └─ angular.json
│
├─ tickets/      # generated PDF tickets
├─ uploads/      # uploaded files
├─ mvnw / mvnw.cmd
├─ pom.xml
└─ README.md
```

---

## Build, tests, and run

Back end:
```bash
# Dev
./mvnw spring-boot:run

# Build jar
./mvnw clean package
java -jar target/app.jar

# Tests
./mvnw test
```

Front end:
```bash
cd frontend

# Dev (with proxy)
npm run start
# or: ng serve --proxy-config proxy.conf.json

# Lint
npm run lint

# Unit tests
npm run test

# Production build
npm run build
# Artifacts in: frontend/dist/<app-name>/
```

---

## Deployment

Scenario 1 — Front and API on the same domain (recommended):
- Serve the API under `/api` (reverse proxy Nginx/Traefik -> backend:8081).
- Serve Angular build (`frontend/dist`) as static files (e.g., Nginx).
- In Angular `environment.prod.ts`: `apiBaseUrl: '/api'`.
- Cookies: `Secure=true`, `SameSite=None` if cross‑subdomain; otherwise `Lax` may be sufficient.

Scenario 2 — Separate domains:
- `environment.prod.ts`: absolute `apiBaseUrl` (e.g., `https://api.example.com/api`).
- Enable CORS on the back end with `allow-credentials=true` and `allowed-origins=https://app.example.com`.
- Cookies: `Secure=true`, `SameSite=None`, optionally `Domain=.example.com` for subdomain sharing.

Spring Boot static option:
- You can copy the Angular build into `src/main/resources/static` to serve the front directly via Spring Boot. Configure SPA fallback at the server/proxy level.

---

## Troubleshooting

- CORS/401:
  - In dev, use the Angular proxy (`proxy.conf.json`) and `withCredentials`.
  - In prod, verify `allowed-origins`, `allow-credentials`, and cookie `SameSite`/`Secure`.

- Cookie not sent:
  - Call the API from the same origin (proxy) or enable `withCredentials`.
  - Ensure `Set-Cookie` isn’t blocked by CORS.
  - In HTTPS production, `Secure=true` is required when `SameSite=None`.

- Tickets/uploads not generated:
  - Check write permissions for `tickets/` and `uploads/`.
  - Verify configured paths and PDF generation services.

- Front cannot reach API:
  - Check `environment*.ts` (`apiBaseUrl`) and reverse proxy configuration.

Logs:
- Adjust `logging.level.*` in `application.properties` and inspect Angular console/network logs.

---

## Contributing

Contributions are welcome:
1. Create a feature branch from `main`
2. Write clear commits and keep tests up to date
3. Follow code style (lint)
4. Open a PR describing your changes

---



