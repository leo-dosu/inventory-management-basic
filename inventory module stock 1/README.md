# Inventory module (In Stock)

Java 25 · Vaadin Flow 25.3.3 · Spring Boot 4.1.2 · Spring Data JPA · Spring Security · MySQL 8.4 · Maven 3.9.11 (wrapper) · JUnit + Mockito

One Maven project: package `com.group3.inventory`, same layout as the group's code.

## 1. MySQL (once)
    CREATE DATABASE inventory_management;
    CREATE USER 'inventory_app'@'localhost' IDENTIFIED BY 'choose-a-password';
    GRANT ALL PRIVILEGES ON inventory_management.* TO 'inventory_app'@'localhost';

## 2. Maven Wrapper (once, needs Maven installed)
    mvn wrapper:wrapper -Dmaven=3.9.11 -Dtype=only-script
This creates `mvnw` and `mvnw.cmd`. After that use `mvnw` instead of `mvn`.

## 3. Run
Set the password, then start:

    Windows PowerShell:  $env:DB_PASSWORD="choose-a-password"
                         .\mvnw.cmd spring-boot:run
    Mac/Linux:           DB_PASSWORD=choose-a-password ./mvnw spring-boot:run

In IntelliJ: add `DB_PASSWORD` under Run > Edit Configurations > Environment variables, then run `InventoryApplication`.

Open http://localhost:8080 and sign in with `admin` / `admin123`
(change with `APP_USER` / `APP_PASSWORD`). Sample rows are added on the first run.

## Tests
    .\mvnw.cmd test
