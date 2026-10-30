# Vendor Contract & Pricing Management (not built yet)

**Work packages:** CON-1 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 7
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "2. Vendor Contract & Pricing Management" (in the lecturer's repository)

## What it is

Stores vendor contracts and agreed terms (prices, discounts, payment terms, delivery commitments, validity dates) and links them to the vendor and to the products they cover.

## Suggested shape

```
contracts/domain/        entities
contracts/repository/    Spring Data repositories
contracts/service/       the business rules, with a test class per service
contracts/ui/            views and dialogs, plus a small ContractsNav class for the sidebar
src/main/resources/META-INF/resources/styles/contracts.css    this feature's styling
src/test/java/com/inventory/pos/contracts/                    your tests
```

**Data:** `VendorContract` (vendor, start and end date, terms text), `ContractPrice` (product, agreed unit price, discount percent).

**Screens:** Contracts on the vendor profile, with an "expiring soon" highlight.

## What it may use

`vendors`. Later, purchasing can pre-fill a PO line with the agreed price.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("contracts")`, `layout = MainLayout.class` and `@StyleSheet("styles/contracts.css")` on your main view.
2. Add a `ContractsNav` class that implements `NavEntry` with `order() = 110`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- You can add a contract with prices for products and see which are expiring.
- A service method returns the agreed price for a vendor and product on a given date.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
