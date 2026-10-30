# Vendor Payables & Payment Tracking (not built yet)

**Work packages:** PAY-1 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 7
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "1. Vendor Payables & Payment Tracking" (in the lecturer's repository)

## What it is

Tracks what the business owes each vendor, when each payment is due, and what has been paid. It records payments, partial payments, credits and the running balance per vendor.

## Suggested shape

```
payables/domain/        entities
payables/repository/    Spring Data repositories
payables/service/       the business rules, with a test class per service
payables/ui/            views and dialogs, plus a small PayablesNav class for the sidebar
src/main/resources/META-INF/resources/styles/payables.css    this feature's styling
src/test/java/com/inventory/pos/payables/                    your tests
```

**Data:** `VendorBill` (vendor, linked PO, amount, due date), `VendorPayment` (bill, amount, date, method).

**Screens:** Like Debt Recovery but for money we owe: cards for total payable and overdue, a list of vendors with balances, and a record-payment dialog.

## What it may use

`vendors` and `purchasing`. The Debt Recovery screen (`debt/`) is the closest example to copy from: overdue logic, oldest-first allocation, and the two-step confirm.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("payables")`, `layout = MainLayout.class` and `@StyleSheet("styles/payables.css")` on your main view.
2. Add a `PayablesNav` class that implements `NavEntry` with `order() = 90`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- Receiving a PO creates a bill due after the vendor payment terms.
- You can part-pay a bill, and the vendor balance and overdue totals update.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
