# Sales Reporting & Analytics (not built yet)

**Work packages:** REP-1, REP-2 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 6
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "1. Sales Reporting & Analytics" (in the lecturer's repository)

## What it is

Turns recorded transactions into reports and dashboards: sales by period, product, category, cashier, payment method and location, plus best- and worst-selling items and stock status.

## Suggested shape

```
salesreports/domain/        entities
salesreports/repository/    Spring Data repositories
salesreports/service/       the business rules, with a test class per service
salesreports/ui/            views and dialogs, plus a small SalesreportsNav class for the sidebar
src/main/resources/META-INF/resources/styles/salesreports.css    this feature's styling
src/test/java/com/inventory/pos/salesreports/                    your tests
```

**Data:** No new tables: read-only queries over `core` invoices, lines and payments.

**Screens:** A report page with a date range and tabs (by day, by product, by payment method), plus top and bottom sellers.

## What it may use

`invoices`, `core`. Groups 4 and 8 build a separate "Report statistics" screen in their own projects: do not copy their work, this one is the POS-side report.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("salesreports")`, `layout = MainLayout.class` and `@StyleSheet("styles/salesreports.css")` on your main view.
2. Add a `SalesreportsNav` class that implements `NavEntry` with `order() = 130`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- Pick a date range and see sales totals by day and by product.
- Best and worst sellers are listed, and the numbers match the Invoices screen.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
