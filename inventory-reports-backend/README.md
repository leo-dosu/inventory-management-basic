# Inventory Reports: Backend (Group 8)

A standalone Spring Boot REST API for the **Report statistics** screen. The screen itself lives in its own project,
`inventory-reports-frontend` (Group 4), and talks to this one over HTTP.

| Part | Standard |
|---|---|
| Java | 25 LTS |
| Spring Boot | 4.1.2 |
| Build | Maven 3.9.11 through the wrapper (`mvnw`), nothing to install |
| Database | H2 in memory for development, MySQL 8.4 through the `mysql` profile |

## Run it

```
./mvnw spring-boot:run
```

It listens on http://localhost:8080. The default `dev` profile fills H2 with two weeks of demo data. Run the tests with
`./mvnw test`.

### MySQL

Set `DB_USERNAME` and `DB_PASSWORD` in your environment, then start with `--spring.profiles.active=mysql`. Neither is
stored in the repo.

## API

There is no login: login and registration belong to another group. Anything that can reach this server can call it,
so run it on your own machine only.

| Endpoint | Returns |
|---|---|
| `GET /api/reports/today` | Four metrics, each `{value, changePercent}` against yesterday: `sales`, `totalOrders`, `revenue`, `customers` |
| `GET /api/reports/orders-vs-sales?days=7` | One `{date, orders, sales}` per day, oldest first, zeros for quiet days (1 to 366 days) |
| `GET /api/reports/top-selling?limit=5` | `{productId, sku, name, unitsSold, revenue}`, best first |

A bad parameter returns 400 with a problem-detail body.

### What the numbers mean

- **sales**: units sold in completed orders. **totalOrders**: every order placed that day. **revenue**: value of completed orders. **customers**: distinct customers who ordered.
- **changePercent** compares today with yesterday. No orders yesterday counts as +100%, or 0% if there are still none.
- **Orders vs sales** counts every order placed that day, and sums the value of the completed ones.
- **Top selling** ranks products by units sold in completed orders only.

## Layout

```
src/main/java/com/inventory/reporting/
  api/          the service interface and the records the endpoints return
  domain/       JPA entities: Product, Customer, SalesOrder, OrderItem
  repository/   Spring Data repositories
  service/      the report logic
  web/          REST controllers and error handling
  config/       the Clock bean
  demo/         dev-only demo data
```

## Not built yet

Anything outside Report statistics: login and users, the Dashboard, and the Orders, In stock and Products data.
