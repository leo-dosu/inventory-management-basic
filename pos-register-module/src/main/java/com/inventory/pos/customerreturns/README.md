# Customer Returns, Refunds & Receipt Management (not built yet)

**Work packages:** CRET-1, CRET-2 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 6
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "2. Customer Returns, Refunds & Receipt Management" (in the lecturer's repository)

## What it is

Handles customers sending items back: looking up the original sale, processing a refund or exchange, deciding what happens to the returned item, and producing printed or digital receipts for all transactions.

## Suggested shape

```
customerreturns/domain/        entities
customerreturns/repository/    Spring Data repositories
customerreturns/service/       the business rules, with a test class per service
customerreturns/ui/            views and dialogs, plus a small CustomerreturnsNav class for the sidebar
src/main/resources/META-INF/resources/styles/customerreturns.css    this feature's styling
src/test/java/com/inventory/pos/customerreturns/                    your tests
```

**Data:** `CustomerReturn` (original invoice, date, refund method, total), `CustomerReturnLine` (invoice line, quantity, RESTOCK or DISCARD).

**Screens:** A "Return" action on an invoice, a return dialog, and a receipt PDF for sales and refunds.

## What it may use

`invoices` and `core`. A RESTOCK line raises `Product.stock`. A refund on an invoice that is not fully paid should reduce the balance owed first. Check with DEBT work packages so the rules agree.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("customerreturns")`, `layout = MainLayout.class` and `@StyleSheet("styles/customerreturns.css")` on your main view.
2. Add a `CustomerreturnsNav` class that implements `NavEntry` with `order() = 120`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- You can return part of an invoice and refund or restock.
- You cannot return more than was sold, and a returned invoice shows it.
- A receipt PDF can be downloaded for any sale or return.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
