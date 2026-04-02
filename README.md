# E-commerce Order Management System

A backend REST API built with Java and Spring Boot for managing products, 
users, and orders in an e-commerce platform.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 4.2 | Application framework |
| Hibernate ORM | Object-relational mapping |
| Spring Data JPA | Repository layer |
| MySQL | Database |
| Maven | Build tool |
| Git | Version control |

## Architecture
```
Controller Layer  →  REST API endpoints (HTTP in/out)
Service Layer     →  Business logic, design patterns
Repository Layer  →  Database access via Spring Data JPA
Entity Layer      →  Database table mappings via Hibernate ORM
```

## Design Patterns Used

- **Strategy Pattern** — Discount calculation (Regular 0%, Premium 10%, Employee 20%)
- **Factory Pattern** — `DiscountStrategyFactory` selects the correct strategy based on user type
- **DAO Pattern** — Repository interfaces abstract all database operations
- **MVC Pattern** — Clean separation of Controller, Service, and Repository layers

## REST API Endpoints

### Products
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/search?name=` | Search products by name |
| POST | `/api/products` | Add new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

### Users
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/register` | Register new user |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users` | Get all users |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Place a new order |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/user/{userId}` | Get all orders for a user |
| GET | `/api/orders` | Get all orders |
| PATCH | `/api/orders/{id}/status` | Update order status |
| PATCH | `/api/orders/{id}/cancel` | Cancel an order |

## Key Business Logic

- **Fail-fast validation** — User and product existence verified before any DB writes
- **Transactional integrity** — `@Transactional` ensures all-or-nothing order placement
- **Stock management** — Real-time stock reduction on order placement
- **Price snapshot** — `priceAtPurchase` stored at order time to preserve history
- **Order state rules** — Only PENDING orders can be cancelled

## How to Run

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Setup
1. Create database:
```sql
CREATE DATABASE ecommerce_db;
```

2. Update `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run:
```bash
mvn spring-boot:run
```

App starts on `http://localhost:8080`

## Sample API Usage

### Place an Order
```json
POST /api/orders
{
  "userId": 1,
  "productQuantities": {
    "1": 2,
    "2": 1
  }
}
```

### Response
```json
{
  "id": 1,
  "status": "PENDING",
  "totalAmount": 47700.0,
  "userName": "Kranthi Ram",
  "items": [...]
}
```

## Author

**Veera Venkata Kranthi Ram Alugula**  
[LinkedIn](https://linkedin.com/in/alugula-kranthiram) | 
[GitHub](https://github.com/Kranthiram)
