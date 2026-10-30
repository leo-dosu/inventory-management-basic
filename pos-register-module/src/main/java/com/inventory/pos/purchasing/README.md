# Purchase Order Creation & Tracking (not built yet)

**Work packages:** PO-1, PO-2 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 7
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "1. Purchase Order Creation & Tracking" (in the lecturer's repository)

## What it is

Lets authorised staff create formal purchase orders (POs) to vendors, send them, get them approved, and track their status from draft to closed.

## Suggested shape

```
purchasing/domain/        entities
purchasing/repository/    Spring Data repositories
purchasing/service/       the business rules, with a test class per service
purchasing/ui/            views and dialogs, plus a small PurchasingNav class for the sidebar
src/main/resources/META-INF/resources/styles/purchasing.css    this feature's styling
src/test/java/com/inventory/pos/purchasing/                    your tests
```

**Data:** `PurchaseOrder` (number like PO-2026-001, vendor, status DRAFT, SENT, PARTLY_RECEIVED, RECEIVED, CLOSED or CANCELLED, expected date), `PurchaseOrderLine` (product, quantity ordered, quantity received, unit cost).

**Screens:** A PO list with status chips, a create-PO dialog (pick vendor, add lines), and a Receive dialog.

## What it may use

`vendors`, and `core` Product. **Receiving goods must raise `Product.stock`** and a PO can be received in parts. That is how stock goes up, the mirror of a sale taking it down.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("purchasing")`, `layout = MainLayout.class` and `@StyleSheet("styles/purchasing.css")` on your main view.
2. Add a `PurchasingNav` class that implements `NavEntry` with `order() = 80`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- You can create a PO, send it, and receive it in one or more deliveries.
- Receiving updates the product stock shown on the Inventory screen.
- You cannot receive more than was ordered, and cannot edit a PO after it is sent.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
