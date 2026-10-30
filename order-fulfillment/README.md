# Order Fulfillment & Logistics (Java/Vaadin)

A starter implementation of the group's orders and delivery management UI using Java Vaadin Flow, Spring Boot, JPA and MySQL. **Not a completed inventory integration.**

## Run
1. Install JDK 25, MySQL 8.4 and Maven 3.9.11 (or the approved class Maven wrapper).
2. Create a MySQL account/database or configure `DB_URL`, `DB_USER`, `DB_PASSWORD` environment variables.
3. Run `mvn spring-boot:run` in this folder and open http://localhost:8080.
4. Run `mvn test` for unit tests.

## Integration notes
This standalone development scaffold should be incorporated into the shared modular monolith by the platform team. Replace the temporary open security configuration with the platform's actual authentication and authorization. Coordinate with inventory module owners to implement stock checks, reservations and stock deductions. Add fulfillment workflow rules, imports/exports, shipments as separate entities, proper migrations and integration tests before production use. Avoid changing platform dependencies without approval.

## Security warning
The current security config allows all requests for local classroom prototyping. Do not deploy to a public server or use real customer information until authentication, authorization and CSRF protection are configured.

## Verification
Source has not been compiled here because Java 25 and Maven are not available in the execution environment. The specified versions may need confirmation with the platform team.
