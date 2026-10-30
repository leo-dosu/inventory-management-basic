# Vendor Returns & Claims Management (not built yet)

**Work packages:** VRET-1 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 7
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "2. Vendor Returns & Claims Management" (in the lecturer's repository)

## What it is

Manages sending defective, damaged, expired or wrongly delivered goods back to the vendor (return to vendor, RTV), and tracking the credit, replacement or refund the vendor owes in return.

## Suggested shape

```
vendorreturns/domain/        entities
vendorreturns/repository/    Spring Data repositories
vendorreturns/service/       the business rules, with a test class per service
vendorreturns/ui/            views and dialogs, plus a small VendorreturnsNav class for the sidebar
src/main/resources/META-INF/resources/styles/vendorreturns.css    this feature's styling
src/test/java/com/inventory/pos/vendorreturns/                    your tests
```

**Data:** `VendorReturn` (vendor, linked PO, reason DEFECTIVE, DAMAGED, EXPIRED or WRONG_ITEM, status, expected resolution CREDIT, REPLACEMENT or REFUND), `VendorReturnLine` (product, quantity).

**Screens:** A returns list, a raise-return dialog, and a "mark resolved" action.

## What it may use

`vendors`, `purchasing`, `payables`. A return takes stock down, and a CREDIT resolution reduces what is owed in payables.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("vendorreturns")`, `layout = MainLayout.class` and `@StyleSheet("styles/vendorreturns.css")` on your main view.
2. Add a `VendorreturnsNav` class that implements `NavEntry` with `order() = 100`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- You can raise a return against a PO and see its status.
- Returning goods lowers stock, and cannot return more than was received.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
