# Ordering Service – README

Part of the MTOGO Microservice Platform  
Designed with DDD + Hexagonal Architecture, fully Dockerized, and integrated with JWT Security, RabbitMQ events, and Swagger/OpenAPI.

## 1. Overview
The Ordering Service manages customer orders end-to-end:
- Shopping carts: add/remove menu items
- Creating and confirming orders
- Secured access using JWT (tokens issued by Customer Service)
- Publishes events to RabbitMQ (Fulfillment, Notification, Dashboard)
- Persists data in MySQL; reads menu/customer via other services (optional v1)

## You MUST run: before building the Docker image.

mvn clean package -DskipTests

## Build & Run Docker containers
From the ROOT project:
docker-compose -f docker-compose.ordering.yml up --build


## 2. Architecture (Hexagonal)
```
ordering-service
├── domain
│   ├── model            # Entities, Value Objects
│   ├── port
│   │   ├── in           # Use case interfaces
│   │   └── out          # Database + Messaging ports
│   └── service          # Domain logic
│
├── application
│   └── service          # Application services (orchestration, if any)
│
├── infrastructure
│   ├── adapter
│   │   ├── in
│   │   │   └── web      # REST controllers
│   │   └── out
│   │       ├── persistence   # JPA, MySQL
│   │       └── messaging     # RabbitMQ
│   └── config                # Beans, Swagger, Security
│
└── test
    ├── unit
    ├── controller
    ├── h2
    └── testcontainers
```
Benefits: clear separation, testable with mock adapters, easy to swap DB/messaging, microservice-friendly.

## 3. Technologies
| Component   | Technology              |
|-------------|-------------------------|
| Language    | Java 21                 |
| Framework   | Spring Boot 3           |
| Architecture| Hexagonal + DDD         |
| Database    | MySQL                   |
| Messaging   | RabbitMQ                |
| Auth        | JWT (from Customer Svc) |
| Docs        | Swagger / OpenAPI       |
| Testing     | JUnit, MockMvc, H2, Testcontainers |
| Deployment  | Docker + Docker Compose |

## 4. Authentication
- Service verifies JWT tokens issued by Customer Service.
- Required header: `Authorization: Bearer <token>`
- Used for protected endpoints (e.g., `/orders` create, `/orders/me`, cart flows).
- Invalid/missing token → `401 Unauthorized`.

## 5. Swagger / OpenAPI
- UI: `http://localhost:8082/swagger-ui/index.html`
- Includes endpoints, schemas, examples, JWT usage, error codes.

## 6. How to Run
### Local (Maven)
```bash
cd microservices/ordering-service
mvn spring-boot:run
# app at http://localhost:8082
```

### Docker Compose
Compose file: `docker-compose.ordering.yml`
```bash
docker-compose -f docker-compose.ordering.yml up --build
```
Services:
- ordering-service (8082 -> 8080)
- ordering-db (MySQL)
- rabbitmq (5673/15673)

Ensure `.env.ordering` has DB/RabbitMQ/JWT values.

## 7. API (high level)
- **POST /orders** – create order
- **GET /orders/{id}** – fetch order by id
- **GET /orders/me** – orders for current user (JWT)
- Cart endpoints (if exposed): create/update/remove/checkout

## 8. RabbitMQ Events
- Exchange: `order.exchange`
- Routing: `order.confirmed`
- Payload example:
```json
{
  "orderId": 9003,
  "customerId": 1,
  "restaurantId": 10,
  "total": "139.90",
  "status": "CONFIRMED",
  "items": [ { "menuItemId": 1, "name": "Burger", "price": "9.99", "quantity": 2 } ],
  "timestamp": "2025-12-02T19:00:00Z"
}
```
Consumers: Fulfillment, Notification, Dashboard.

## 9. Testing
- All tests: `mvn clean test`
- Unit only: `mvn -Dtest=*UnitTest test`
- Controller (MockMvc): `mvn -Dtest=*ControllerTest test`
- H2 JPA tests included
- Testcontainers (MySQL) require Docker; auto-skips if Docker unavailable.


- Checkout: `POST /cart/{cartId}/checkout`
- My orders: `GET /orders/me`

## 11. Postman
- Collection: `postman/MTOGO_Ordering_Service.postman_collection.json`
- Contains cart/order flows, events, JWT examples.
