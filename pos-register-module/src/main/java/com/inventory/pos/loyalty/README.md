# Customer Management & Loyalty (not built yet)

**Work packages:** LOY-1 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 6
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "1. Customer Management & Loyalty" (in the lecturer's repository)

## What it is

Stores customer profiles and purchase history, and supports loyalty rewards such as points, store credit and gift cards.

## Suggested shape

```
loyalty/domain/        entities
loyalty/repository/    Spring Data repositories
loyalty/service/       the business rules, with a test class per service
loyalty/ui/            views and dialogs, plus a small LoyaltyNav class for the sidebar
src/main/resources/META-INF/resources/styles/loyalty.css    this feature's styling
src/test/java/com/inventory/pos/loyalty/                    your tests
```

**Data:** `LoyaltyAccount` (customer, points balance), `LoyaltyEntry` (earned or redeemed, amount, invoice), `StoreCredit` (customer, balance).

**Screens:** Points and store credit on the customer card, and a redeem option at checkout.

## What it may use

`customers`. Redeeming at checkout touches the register, so agree the hook with the REG-5 owner.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("loyalty")`, `layout = MainLayout.class` and `@StyleSheet("styles/loyalty.css")` on your main view.
2. Add a `LoyaltyNav` class that implements `NavEntry` with `order() = 140`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- A customer earns points on a paid sale and can redeem them.
- Points never go negative and appear in the customer history.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
