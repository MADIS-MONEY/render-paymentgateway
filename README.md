# MadisFinance Payment Gateway Service - MTN, ORANGE, MOOV, WAVE and HUB2 API
This project provides an API for handling payment operations (Cash-in/Cash-out) in the Madis Wallet system. The API offers endpoints to create and manage payment intents, transfer intents, and to retrieve the status of transactions via Hub2 (MTN, ORANGE, MOOV, WAVE and HUB2) services.

## Features

- **Cash-in Intents**: Create and attempt payment intents.
- **Cash-out Transfers**: Create and manage transfer intents.
- **Transaction Status**: Retrieve transaction status for both cash-in and cash-out operations.
- **Update Transaction Status**: Update the status of payment or transfer transactions.

## API Endpoints

### 1. Create Cash-in Payment Intent

- **URL**: `/hub2/cashin-intents`
- **Method**: `POST`
- **Request Body**: `PaymentIntent` (JSON)
- **Response**: `CashinResponse`, containing the status and details of the payment intent.

### 2. Attempt Cash-in Payment

- **URL**: `/hub2/cashin-attempts/{id}`
- **Method**: `POST`
- **Path Variables**:
  - `id`: Payment intent ID.
- **Request Body**: `Hub2PaymentIntentAttemptRequest` (JSON)
- **Response**: `CashinResponse`, containing the result of the payment attempt.

### 3. Update Payment Status

- **URL**: `/hub2/cashin-attempts/{transactionId}/{status}`
- **Method**: `PUT`
- **Path Variables**:
  - `transactionId`: Transaction ID to update.
  - `status`: New status to be set for the transaction.
- **Response**: Boolean, indicating whether the status was updated successfully.

### 4. Retrieve Payment Intent by ID

- **URL**: `/hub2/cashin-attempts/{id}`
- **Method**: `GET`
- **Path Variables**:
  - `id`: Payment intent ID.
- **Response**: Map containing the payment intent details.

### 5. Retrieve Payment Intents by Status

- **URL**: `/hub2/cashin-attempts/all/{status}`
- **Method**: `GET`
- **Path Variables**:
  - `status`: Status of the payment intents to retrieve.
- **Response**: List of payment intents matching the provided status.

### 6. Create Cash-out Transfer

- **URL**: `/hub2/cashout-transfer`
- **Method**: `POST`
- **Request Body**: `TransferIntent` (JSON)
- **Response**: `CashinResponse`, containing the status and details of the transfer.

### 7. Check Transfer Status

- **URL**: `/hub2/cashout-transfer/check`
- **Method**: `POST`
- **Request Body**: Set of transaction IDs (JSON)
- **Response**: Map containing the status of each transaction ID.

### 8. Update Transfer Status

- **URL**: `/hub2/cashout-transfer/{transactionId}/{status}`
- **Method**: `PUT`
- **Path Variables**:
  - `transactionId`: Transfer transaction ID to update.
  - `status`: New status to be set for the transfer.
- **Response**: Boolean, indicating whether the status was updated successfully.

### 9. Retrieve Transfer Intents by Status

- **URL**: `/hub2/cashout-transfer/all/{status}`
- **Method**: `GET`
- **Path Variables**:
  - `status`: Status of the transfer intents to retrieve.
- **Response**: List of transfer intents matching the provided status.

## Setup

### Prerequisites

- Java 11+
- Spring Boot
- Maven


## Usage

- **Cash-in Operations**: Use the `/hub2/cashin-intents` and related endpoints to handle cash-in payment intents and status updates.
- **Cash-out Operations**: Use the `/hub2/cashout-transfer` and related endpoints to manage cash-out transfers and their statuses.

## Dependencies

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Swagger](https://swagger.io/) for API documentation

## License

This project is licensed under the MIT License - see the [Madis](GNU) file for details.

## Contact

For questions or support, feel free to reach out via email at `[yannickstephane.ehu@gmail.com]` or submit an issue in the repository.


### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MADIS-MONEY/madis-paymentgateway-service.git

2. Navigate to the project directory:
   ```bash
   cd madis-paymentgateway-service
   
3. Build the project with Maven:
   ```bash
   mvn clean install

4. Run the application:
   ```bash
   mvn spring-boot:run

