# Customer Service – REST API Blueprint

This document describes the REST API for the Customer Service microservice. It focuses on endpoints, request/response models, status codes, and JWT security.



## Base URL

- Local direct: `http://localhost:8081`
- Via gateway (example): `http://localhost:9000/customer-service`

Use the gateway prefix if routing through an API gateway.



## Authentication (JWT)

- Successful register/login returns a JWT.
- Send on protected endpoints:

```
Authorization: Bearer <jwt-token>
```

Token payload (simplified): `sub` = customerId (string), `exp` = expiry; signed with a secret (`JWT_SECRET`).

Missing/invalid token → `401 Unauthorized`.

---

## Data Models
http://localhost:8081/api/auth/register
**Customer**
```json
{
  "id": 1,
  "fullName": "Sara Example",
  "email": "sara@example.com",
  "phone": "12345678",
  "street": "Main Street 1",
  "city": "Copenhagen",
  "postalCode": "2100",
  "accountStatus": "ACTIVE",
  "verificationStatus": "VERIFIED"
}
```

**Register Request**
```json
{
  "fullName": "Sara Example",
  "email": "sara@example.com",
  "password": "123456",
  "phone": "12345678",
  "street": "Main St 1",
  "city": "Copenhagen",
  "postalCode": "2100"
}
```

**Login Request**
http://localhost:8081/api/auth/login
```json
{ "email": "sara@example.com", "password": "123456" }
```

**Token Response**
```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp..." }
```

**Update Profile Request**
```json
{
  "fullName": "New Name",
  "phone": "11122233",
  "street": "New Street 10",
  "city": "Copenhagen",
  "postalCode": "2100"
}
```

**Generic Error**
```json
{
  "error": "Bad Request",
  "message": "Email already in use",
  "status": 400,
  "timestamp": "2025-11-30T12:34:56Z"
}
```

---

## Endpoints – Auth

### POST /auth/register
- Creates account, hashes password, generates verification token, emits event, sends mock email.
- Body: Register Request.
- Success: `201 Created`, returns created customer (and verification token is emailed/mocked).
- Errors: `400` invalid input, `409` email exists.

### POST /auth/login
- Authenticates and returns JWT.
- Body: Login Request.
- Success: `200 OK` + Token Response.
- Errors: `401` bad credentials; `403` if account not verified.

### GET /auth/verify?token=<verification-token>
- Verifies email by token.
- Success: `200 OK` text `"Account verified successfully!"`.
- Errors: `400/404` invalid or missing token.

---

## Endpoints – Customer Profile (JWT required)

### GET /customers/me
- Returns the authenticated customer profile using `sub` from JWT.
- Success: `200 OK` + Customer.
- Errors: `401` invalid/missing token; `404` customer not found.

### PUT /customers/me
- Updates profile fields (name, phone, address).
- Body: Update Profile Request.
- Success: `200 OK` + updated Customer.
- Errors: `400` invalid data; `401` unauthorized; `404` customer not found.

---

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- Flow: register/login → copy token → Authorize in Swagger UI → call protected endpoints.

---

## Error Handling (summary)

| Status | Meaning                         | Example                          |
|--------|---------------------------------|----------------------------------|
| 400    | Invalid input                   | Bad JSON, missing required field |
| 401    | No/invalid JWT                  | Missing or expired token         |
| 403    | Forbidden (unverified)          | Unverified user access           |
| 404    | Not found                       | Token or customer missing        |
| 409    | Conflict                        | Email already in use             |

---

## Technical Notes

- Architecture: Ports & Adapters, DDD-style Customer aggregate.
- Security: Spring Security + JWT filter; BCrypt for passwords.
- Persistence: MySQL; JPA entities mapped to domain.
- Messaging: RabbitMQ (customer.registered / customer.verified events).
- Docs: springdoc-openapi.
