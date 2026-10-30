# Vendor Communication Log & Supplier Portal (not built yet)

**Work packages:** COM-1 in [docs/WORK-PACKAGES.md](../../../../../../../docs/WORK-PACKAGES.md)
**Suggested for:** Group 7
**Lecturer's spec:** `POS_and_Vendor_Relations_Consolidated.md`, section "2. Vendor Communication Log & Supplier Portal" (in the lecturer's repository)

## What it is

Keeps all communication with each supplier in one place. Stretch goal: a limited login (portal) where vendors can confirm orders, update their own details and see order status.

## Suggested shape

```
vendorcomms/domain/        entities
vendorcomms/repository/    Spring Data repositories
vendorcomms/service/       the business rules, with a test class per service
vendorcomms/ui/            views and dialogs, plus a small VendorcommsNav class for the sidebar
src/main/resources/META-INF/resources/styles/vendorcomms.css    this feature's styling
src/test/java/com/inventory/pos/vendorcomms/                    your tests
```

**Data:** `CommunicationEntry` (vendor, when, channel such as CALL, EMAIL or WHATSAPP, who from our side, summary, optional link to a purchase order).

**Screens:** A timeline on each vendor, with an "add note" dialog. Filter by channel.

## What it may use

`vendors`. The portal needs a login, which does not exist in this app yet, so build the log first.

The package rules in `ArchitectureTest` decide which packages you may import. If you need something from a package that
is not allowed, move the shared part into `core` or `shared` (ask the lead), do not import across.

## How it plugs into the app (no shared file needs editing)

1. Put `@Route("vendorcomms")`, `layout = MainLayout.class` and `@StyleSheet("styles/vendorcomms.css")` on your main view.
2. Add a `VendorcommsNav` class that implements `NavEntry` with `order() = 70`. Copy `register/ui/RegisterNav.java`.
3. Extend `PosDataTest` for service tests (see `debt/service/DebtServiceTest.java`).

## Done when

- You can log a conversation against a vendor and see the history, newest first.
- Entries cannot be edited after saving (it is an audit trail), only added.
- `./mvnw test` passes, including `ArchitectureTest`.
- The screen looks like the others (use `Panels`, `FilterChips`, `Format`; see `debt/ui/DebtView.java`).
