# 📦 Order Processing Service

This project is a **Spring Boot 4** API focused on asynchronous event-driven order processing. The workflow consists of receiving an order request, publishing it to an **Amazon SQS** queue, consuming it asynchronously, and persisting the data into a **MySQL** database while ensuring idempotency.

## 🚀 Technologies Used

* **Java 17**
* **Spring Boot 4.1.1**
* **Spring Data JPA** & **MySQL** (Persistence)
* **Spring Cloud AWS SQS 4.1.1** (Messaging)
* **LocalStack 3.0.0** (Local AWS SQS emulation)
* **Docker & Docker Compose** (Environment containerization)
* **Lombok** (Productivity/Boilerplate reduction)

---

## 🛠️ Architecture and Data Flow

1. **`POST /v1/orders`**: The client sends an order request (`OrderRequest`).
2. **Publisher**: `OrderPublisherService` generates a unique UUID for the `eventId` and publishes the message (`OrderEvent`) to the SQS queue named `order-queue`. The API responds immediately with a `202 Accepted` status.
3. **Consumer**: `OrderConsumer` listens to the `order-queue`, consumes the message, and passes it to the processor.
4. **Processor**: `OrderProcessorService` validates if the `eventId` has already been processed (**Idempotency**). If it is a new event, it saves the order into the MySQL database with a `PROCESSED` status.
5. **Queries**: The client can query the results using the `GET /v1/orders` and `GET /v1/orders/{id}` endpoints.

---

## ⚙️ How to Run the Project

### 1. Prerequisites
Make sure you have installed:
* Docker
* Java 17
* Maven

### 2. Infrastructure Setup (Automated SQS Creation)
To avoid running manual commands to create the SQS queue on the first startup, you can let LocalStack initialize it automatically:

1. Create a folder named `scripts` in your project root.
2. Inside it, create a file named `init-sqs.sh` with the following content:
   ```bash
   #!/bin/bash
   awslocal sqs create-queue --queue-name order-queue
   ```
3. Update your `docker-compose.yml` to mount this script folder into LocalStack:
   ```yaml
     localstack:
       container_name: localstack_api
       image: localstack/localstack:3.0.0
       ports:
         - "127.0.0.1:4566:4566"
       environment:
         - SERVICES=sqs
         - AWS_DEFAULT_REGION=us-east-1
       volumes:
         - ./scripts:/etc/localstack/init/ready.d
   ```

### 3. Start the Infrastructure (Containers)
In the root directory of the project, run the following command to start MySQL (port `3307`) and LocalStack (port `4566`):
```bash
docker compose up -d
```

### 4. Run the Spring Boot Application
Run the project via Maven or directly through your IDE:
```bash
mvn spring-boot:run
```

---

## 🛣️ API Endpoints

### 1. Create an Order (Asynchronous)
* **URL:** `POST /v1/orders`
* **Request Body (`JSON`):**
  ```json
  {
    "clientId": 123,
    "amount": 250.75
  }
  ```
* **Expected Response:** `202 Accepted`

### 2. List All Orders
* **URL:** `GET /v1/orders`
* **Expected Response:** `200 OK`
  ```json
  [
    {
      "id": 1,
      "clientId": 123,
      "amount": 250.75,
      "status": "PROCESSED",
      "createdAt": "2026-10-01T00:00:00"
    }
  ]
  ```

### 3. Find Order by ID
* **URL:** `GET /v1/orders/{id}`
* **Expected Response:** `200 OK` or `404 Not Found`
