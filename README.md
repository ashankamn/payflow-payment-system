# PayFlow — Event-Driven Payment Processing System

A production-style backend service that simulates real-world payment processing, built to demonstrate event-driven architecture, caching, and containerized infrastructure using the same patterns used in high-volume payment systems (e.g. Western Union-scale transaction processing).

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.9-black)
![Redis](https://img.shields.io/badge/Redis-7-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![CI](https://github.com/ashankamn/payflow-payment-system/actions/workflows/ci.yml/badge.svg)

## What This Demonstrates

- **Event-driven architecture**: payment creation publishes an event to Kafka, processed asynchronously by an independent consumer — decoupling the write path from downstream processing (notifications, fraud checks, ledger updates, etc.)
- **Caching strategy**: payment lookups are cached in Redis, verified to skip the database entirely on repeat reads
- **Containerized infrastructure**: PostgreSQL, Kafka, and Redis all orchestrated via a single `docker-compose up -d` command
- **Automated testing**: JUnit 5 + Mockito unit tests covering service and controller layers, run automatically via CI
- **Clean layered architecture**: Controller → Service → Repository, with DTOs enforcing strict API contracts

## Architecture
┌─────────────┐
HTTP Request --> │  Controller │
└──────┬──────┘
│
┌──────▼──────┐        ┌───────────┐
│   Service   │──cache──▶   Redis   │
└──────┬──────┘        └───────────┘
│
┌────────────┼────────────┐
│                         │
┌──────▼──────┐          ┌───────▼───────┐
│  PostgreSQL │          │  Kafka Topic   │
│  (payments) │          │ payment-events │
└─────────────┘          └───────┬────────┘
│
┌───────▼────────┐
│ Async Consumer │
│  (processing)  │
└────────────────┘
## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 (Web, Data JPA, Validation, Actuator) |
| Database | PostgreSQL 16 |
| Messaging | Apache Kafka (KRaft mode) |
| Caching | Redis 7 |
| Testing | JUnit 5, Mockito |
| CI/CD | GitHub Actions |
| Containerization | Docker, Docker Compose |
| Build Tool | Maven |

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments` | Create a new payment (publishes event to Kafka) |
| GET | `/api/payments` | List all payments |
| GET | `/api/payments/{id}` | Get a payment by ID (Redis-cached) |

## Running Locally

**Prerequisites:** Docker, Java 21, Maven (or use the included `./mvnw` wrapper)

**1. Start infrastructure (PostgreSQL, Kafka, Redis):**
```bash
docker compose up -d
```

**2. Create the Kafka topic (first run only):**
```bash
docker exec -it payflow-kafka kafka-topics --create \
  --topic payment-events \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 1
```

**3. Run the application:**
```bash
cd payflow-service
./mvnw spring-boot:run
```

**4. Test it:**
```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "senderAccount": "ACC-001",
    "receiverAccount": "ACC-002",
    "amount": 150.00,
    "currency": "USD"
  }'
```

## Running Tests

```bash
cd payflow-service
./mvnw test
```

## What's Next

- Fraud-check consumer subscribing independently to `payment-events`
- Idempotency keys to prevent duplicate payment processing
- Deployment to AWS EKS with horizontal pod autoscaling
- Distributed tracing across the async event pipeline

## Author

Built by [Ashanka](https://github.com/ashankamn) as a demonstration of event-driven backend architecture patterns used in production payment systems.
