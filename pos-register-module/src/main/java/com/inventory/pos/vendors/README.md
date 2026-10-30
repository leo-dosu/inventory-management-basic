# Vendor Profile & Master Database (not built yet)

**Work packages:** VEN-1, VEN-2, VEN-3 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 7
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "1. Vendor Profile & Master Database" (in the lecturer's repository)

## What it is

A central record for every supplier the business buys from: contact people, addresses, payment terms, lead times, tax details, status, and linked products, purchase orders and contracts.

## Suggested shape

```
vendors/domain/        entities
vendors/repository/    Spring Data repositories
vendors/service/       the business rules, with a test class per service
vendors/ui/            views and dialogs, plus a small VendorsNav class for the sidebar
src/main/resources/META-INF/resources/styles/vendors.css    this feature's styling
src/test/java/com/inventory/pos/vendors/                    your tests
```

**Data:** `Vendor` (name, tax ID, payment terms in days, lead time in days, status ACTIVE or INACTIVE), `VendorContact` (name, role, phone, email), `VendorProduct` (which `core` Product the vendor supplies, their supplier SKU, usual cost).

**Screens:** A vendor list with search and a status filter, and a vendor profile dialog to add and edit.

## What it may use

`core` for Product. Other vendor features (purchasing, payables, contracts) build on this one, so agree the `Vendor` entity early and push it first.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("vendors")`, `layout = MainLayout.class` and `@StyleSheet("styles/vendors.css")` on your main view.
2. Add a `VendorsNav` class that implements `NavEntry` with `order() = 60`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- You can add, edit and deactivate a vendor and link products to it.
- Search and the status filter work.
- Service tests cover the rules (name required, payment terms between 0 and 365 days).
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
