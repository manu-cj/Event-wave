# Spring Boot API Template – com.manu.template

This project is a Java Spring Boot REST API, featuring:
- JWT authentication
- User management (registration, login)
- Product CRUD with DTO and Mapper
- Role-based security (USER, ADMIN)
- Global validation & error handling
- Swagger/OpenAPI documentation
- H2 database (in-memory, production-ready)

---

## Project structure

```
src/main/java/com/manu/template/
    Application.java
    config/
    controller/
    dto/
    exception/
    mapper/
    model/
    repository/
    security/
    service/
src/main/resources/
    application.properties
pom.xml
```

---

## Running the project

1. **Clone the repository**
2. **Build and run**
   ```sh
   mvn spring-boot:run
   ```
3. **Access Swagger documentation**
    - [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

---

## Authentication & Security

- JWT required (except `/api/auth/register` and `/api/auth/login`)
- Roles: `USER` (default), `ADMIN` (for sensitive endpoints)
- Add the header:
  ```
  Authorization: Bearer <your_jwt_token>
  ```

---

## Main endpoints

### Authentication

- **Register**
  ```
  POST /api/auth/register
  {
    "username": "user",
    "password": "password"
  }
  ```
- **Login (retrieve a JWT)**
  ```
  POST /api/auth/login
  {
    "username": "user",
    "password": "password"
  }
  ```
  Response:
  ```json
  { "token": "..." }
  ```

---

### Products

- **List all**  
  `GET /api/products`
- **Detail**  
  `GET /api/products/{id}`
- **Create**  
  `POST /api/products`  
  _(ADMIN only)_
- **Update**  
  `PUT /api/products/{id}`  
  _(ADMIN only)_
- **Delete**  
  `DELETE /api/products/{id}`  
  _(ADMIN only)_

Example product JSON:
```json
{
  "name": "Product A",
  "price": 19.99
}
```

---

## Error handling

- Automatic DTO validation (400 errors)
- Global handler (`GlobalExceptionHandler`)
- Clear messages for authentication, validation, etc. errors

---

## Database

- Default: **in-memory H2** (console at `/h2-console`)
- Table `users` (avoids reserved word `user`)
- Table `products`
- Association table for user roles

---

## Swagger / OpenAPI

- Interactive documentation:  
  [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

---

## Customization & Production

- For PostgreSQL/MySQL: edit `application.properties`
- Change the JWT secret and expiration time
- Add role and permission management as needed
- To add an admin: modify the registration code, or insert manually in the database

---

## Authors

- Initialized with Copilot for **manu-cj**
- Modifiable, reusable, extensible as you wish!

---

## License

MIT (suggested, adapt as needed)
