# POS and Vendor Relations — Features (ODDE OPEYEMI SAMUEL 122)

## 1. Automated Low-Stock Vendor Reordering
Automatically alerts staff and drafts a purchase order to the relevant
vendor once a product's stock falls below a set threshold, so restocking
isn't dependent on someone remembering to check.

## 2. Offline Mode with sync when back online

- *Business Continuity:* Prevents lost sales and vendor delays during internet outages.
- *Uninterrupted Workflows:* Process purchases, issue receipts, and update stock without internet.
- *Local Caching:* Stores pricing, customer, and vendor data directly on the device.
- *Transaction Queueing:* Saves all offline sales and stock updates in a secure local buffer.
- *Background Sync:* Automatically syncs queued data to the central server when connection returns.
- *Conflict Resolution:* Uses timestamps and inventory locks to resolve overlapping updates.
- *Data Integrity:* Ensures zero lost transactions and accurate ledgers.
