# pos-and-vendor-relations-system-group-7
COM121, Task app manager development
# Feature Updates: POS System

**Name:** Williams Fadejimi Eniola
**Matric No:** F/ND/25/3210016

 # 1. Product Catalog, Variants & Barcode Management

## Description

The master list of everything the business sells. Each product has a SKU, barcode, name, category, price and cost, and can have variants (size, colour) and bundles. Items link to the vendor that supplies them.

## Purpose

Checkout, inventory and purchasing all read from the same product data. Messy or duplicated products lead to wrong prices, wrong stock and orders sent to the wrong vendor.

## How It Works

1. Products are created manually or imported in bulk (for example, from a CSV file).
2. Each product gets a unique SKU and barcode, plus a category and unit of measure.
3. Variants and bundles are defined under a parent product.
4. Each product is linked to one or more vendors with the vendor's item code and cost price.
5. Changes (price, cost, vendor) are saved with a change history.

## Information Required

- Product name, SKU, barcode, category and unit of measure
- Selling price, cost price, and tax category
- Variant attributes and bundle components
- Preferred vendor(s) and vendor item codes
- Product images (optional)

## Output / Action

- A searchable, scannable catalog used by the checkout screen
- Barcode labels that can be printed
- Product-to-vendor links used when creating purchase orders
- Change log of price and cost edits

## Benefits

- One consistent source of product data for sales, stock and purchasing
- Faster checkout through barcode scanning
- Bulk import/edit saves time when many products change at once

## Limitations / Dependencies

- Needs initial data entry or an import file in the right format
- Requires discipline to avoid duplicate SKUs
- Barcode scanning requires a scanner and printed labels or manufacturer barcodes

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




