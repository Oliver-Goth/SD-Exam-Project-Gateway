# MTogo Final Exam Microservices

A quick guide for instructors to set up environment secrets, copy .env files, and run the full stack.

## Prerequisites
- Docker & Docker Compose installed and running.
- (Optional) Java 17+ and Maven if you want to run individual services locally.

## 1) Create strong passwords/secrets
Use unique, long values (16+ chars, mix upper/lower/number/symbol). Avoid reusing the sample secrets in the repo.

## 2) Root RabbitMQ env
Create `./.env` in the repo root (do not commit it):
```
RABBITMQ_HOST=rabbitmq
RABBITMQ_USER=<choose-username>
RABBITMQ_PASSWORD=<strong-password>
```

## 3) Copy each service env template and fill values
Run these copies (PowerShell examples):
```
Copy-Item delivery-service/.env.delivery.example delivery-service/.env.delivery
Copy-Item fulfillment-service/.env.fulfillment.example fulfillment-service/.env.fulfillment
Copy-Item ordering-service/.env.ordering.example ordering-service/.env.ordering
Copy-Item restaurant-service/.env.restaurant.example restaurant-service/.env.restaurant
Copy-Item financial-service/.env.example financial-service/.env
```
Then open each new file and set strong passwords/secrets (replace any `replace_me` or placeholder values). Keep RabbitMQ creds consistent across services.

### Customer service env (create manually)
Create `customer-service/.env` with (replace bracketed values):
```
CUSTOMER_DB_NAME=<db-name>           # e.g., customer_db
CUSTOMER_DB_USER=<db-username>       # e.g., customer_user
CUSTOMER_DB_PASSWORD=<strong-password>
CUSTOMER_DB_ROOT_PASSWORD=<strong-password>
CUSTOMER_DB_PORT=3308                # change if needed
SPRING_DATASOURCE_URL=jdbc:mysql://customer-db:3306/customer_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
JWT_SECRET=<strong-secret>           # long random string
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USER=<same-as-root-env>
RABBITMQ_PASSWORD=<same-as-root-env>
```

Any value wrapped in `<...>` must be replaced locally and kept out of git.

## 4) Build and run everything with Docker Compose
From repo root:
```
docker-compose -f docker-compose.all.yml up --build -d
```
Follow logs:
```
docker-compose -f docker-compose.all.yml logs -f
```

## 5) Stop/cleanup
```
docker-compose -f docker-compose.all.yml down -v
```

