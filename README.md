# Dynamic Invoice Scheduler & Executor Engine

This project is an implementation of an invoice processing system built on an *Event-Driven Microservices* architecture and *Polyglot Persistence*. The system is split into two independent services managed within a *Monorepo* structure:
1. **invoice-scheduler (Port 8080)**: Manages partner schedule contracts using PostgreSQL and optimizes query performance using Redis caching. Acts as the Kafka Producer.
2. **invoice-executor (Port 8081)**: Acts as the Kafka Consumer that processes invoices asynchronously and records the execution audit trail into MongoDB.

---

## Architecture & Ecosystem Components

* **Relational DB**: PostgreSQL 15 (Schema automatically managed via Flyway Migration)
* **In-Memory Data Grid**: Redis 7 (Cache-Aside Strategy for API optimization)
* **Message Broker**: Apache Kafka 3.8 (KRaft Mode - Zookeeperless)
* **NoSQL Document Store**: MongoDB 6.0 (Audit Trail / Invoice History)

---

## Prerequisites

Before running the applications, ensure your machine has the following installed:
* **Java 17** or higher
* **Docker & Docker Compose**

---

## How to Run the Applications (Local Terminal)

Follow these steps to spin up the entire ecosystem on your local machine:

### Step 1: Start the Infrastructure Containers (Docker)
Open your terminal at the root directory of the project (where `compose.yaml` is located) and run:
```bash
docker compose up -d
```
*Ensure that the Postgres, Kafka, Redis, and MongoDB containers are all 'Running' before proceeding to the next step.*

### Step 2: Run the 'invoice-scheduler' Application
Open a new terminal window or tab, navigate to the scheduler sub-project directory, and run:
```bash
cd invoice-scheduler
./gradlew bootRun
```
*This service will bind to port `8080` and automatically execute database schema migrations via Flyway.*

### Step 3: Run the 'invoice-executor' Application
Open another terminal window or tab, navigate to the executor sub-project directory, and run:
```bash
cd invoice-executor
./gradlew bootRun
```
*This service will bind to port `8081` and immediately start listening for events from the Kafka Topic.*

---

## API Testing & System Verification Guide

You can test the end-to-end functionality of the system using `cURL` commands directly from your terminal:

### 1. Test Dynamic Schedule Validation & Redis Caching (Scheduler Layer)
Execute the following `GET` request to simulate searching for active schedules on the month-end date (`2026-06-30`):
```bash
curl -X GET "http://localhost:8080/api/v1/schedules/valid?action=SEND_INVOICE&date=2026-06-30"
```
* **Cache Verification**: On the first execution, the application triggers a *Native Query* to PostgreSQL (`Cache Miss`). On the second execution and onwards, the response returns instantly directly from the Redis Cache (`Cache Hit`).

### 2. Test Batching Engine Simulation (End-to-End Event Driven)
Execute the following `POST` request to trigger the batch engine to scan valid partners and publish them to Kafka:
```bash
curl -X POST "http://localhost:8080/api/v1/schedules/run?action=SEND_INVOICE&date=2026-06-30"
```

**Expected Terminal Log Output:**
* **Scheduler Terminal (8080)** will show successful broker dispatch logs:
  `========== [PRODUCER] Success push Partner ID X to Kafka Topic ==========`
* **Executor Terminal (8081)** will automatically intercept the event from the broker and persist the document into MongoDB:
  `[MONGO] Success writing audit trail with Document ID: [Auto-Generated-UUID]`