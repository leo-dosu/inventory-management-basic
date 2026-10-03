# POS AND VENDOR RELATIONS SYSTEM

## Student Information

**Name:** OLADELE DAVID  
**Matric Number:** F/ND/25/3210356

  # 1. Offline Mode & Data Synchronization

## Description

Lets registers keep selling when the internet connection fails. Transactions are stored locally and automatically sent to the central system when the connection returns.

## Purpose

Network outages should not stop sales. Without offline capability, a business loses revenue and customers during downtime, or staff fall back to paper records that are easily lost.

## How It Works

1. The POS keeps a local copy of the catalog, prices and key settings.
2. When the connection drops, the terminal switches to offline mode and keeps recording sales locally.
3. Payment methods that need online authorisation may be limited; cash and queued payments continue.
4. When the connection returns, queued transactions are uploaded in order.
5. The server updates stock and reports, and flags any conflicts (for example, the same item sold at two terminals while offline).

## Information Required

- Local cached product, price and tax data
- Local storage for pending transactions
- Timestamps and terminal IDs for ordering and conflict checks
- Sync rules and conflict-handling settings

## Output / Action

- Uninterrupted sales during outages
- Automatic upload and reconciliation after reconnection
- Conflict or error alerts for manager review

## Benefits

- No lost sales during network failures
- Reliable records after recovery, with no manual re-entry
- Stock and reports catch up automatically

## Limitations / Dependencies

- Stock levels are temporarily out of date while offline
- Some payment types (cards, wallets) may not work without a connection
- Needs well-designed conflict handling and enough local storage

  # 2. Employee Roles, Permissions & Shift Management

## Description

Controls who can use the system and what they can do. Staff get accounts with role-based permissions (cashier, supervisor, manager, purchasing officer, admin), and the system tracks clock-in/out and sales per employee.

## Purpose

A POS handles money, stock and supplier data. Without access control, anyone can change prices, void sales, or approve vendor payments, and there is no accountability for errors or theft.

## How It Works

1. An admin creates user accounts and assigns each a role.
2. Each role has a defined set of allowed actions (for example, cashiers cannot edit prices; only purchasing staff can create purchase orders).
3. Staff log in with a PIN or password and clock in at the start of a shift.
4. Sensitive actions (voids, large discounts, refunds, payment approvals) require supervisor approval.
5. All actions are logged against the user and time.

## Information Required

- Employee details and assigned roles
- Permission matrix for each role
- Login credentials (PIN/password)
- Shift times and clock-in/out events

## Output / Action

- Authorised or blocked actions based on role
- Audit log of sensitive actions
- Timesheets and per-employee sales summaries

## Benefits

- Reduces fraud and accidental changes
- Clear accountability for every transaction and approval
- Separates duties between selling, purchasing and paying vendors

## Limitations / Dependencies

- Roles must be carefully designed and reviewed
- Weak passwords or shared PINs defeat the control
- Depends on a reliable user directory kept up to date as staff join or leave




