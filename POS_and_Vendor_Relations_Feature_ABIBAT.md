Name : ABIBAT FOLADARA 
Matric number:F/ND/25/3210213
# Feature Updates: POS and Vendor Relations System

# 1. Vendor Profile & Master Database

## Description

A central record for every supplier the business buys from: contact people, addresses, payment terms, lead times, tax details, status, and linked products, purchase orders and contracts.

## Purpose

Supplier details kept in emails, notebooks and spreadsheets go out of date and are hard to find. A single vendor record gives everyone the same correct information and is the base for every other vendor feature.

## How It Works

1. A vendor record is created with business details, contacts and bank/payment terms.
2. The vendor is given a status (active, inactive, on hold) that controls whether it appears when creating purchase orders.
3. Products are linked to the vendor with its item codes and agreed cost prices.
4. Custom categories (for example, "beverages" or "packaging") allow filtering and reporting.
5. The record shows the vendor's purchase orders, contracts, invoices and performance in one place.

## Information Required

- Legal and trading name, address, and country
- Contact persons and communication details
- Payment terms, currency, and lead time
- Tax or registration details
- Linked products and cost prices
- Vendor status and category

## Output / Action

- A searchable vendor directory
- Default payment terms and lead times applied to new purchase orders
- Filtered vendor lists for reporting and review

## Benefits

- One reliable source of supplier information
- Faster purchase order creation using stored defaults
- Easier supplier comparison and reporting by category

## Limitations / Dependencies

- Needs initial data entry and regular clean-up
- Duplicate vendor records must be prevented
- Bank and payment details need restricted access (see *Employee Roles & Permissions*)


# 2. Vendor Communication Log & Supplier Portal

## Description

Keeps all communication with each supplier in one place and gives vendors a limited login (portal) where they can confirm orders, update their own details and see order status.

## Purpose

Order confirmations, delivery changes and price discussions scattered across phone calls and personal emails are easily lost. When a dispute arises, there is no record of what was agreed.

## How It Works

1. Messages, notes and calls with a vendor are logged on the vendor record.
2. POs and updates are sent to the vendor through email or the portal.
3. Vendors log in to the portal to confirm or reject orders, propose delivery dates, and update contacts or documents.
4. Changes made by the vendor are visible to staff, and key changes can require approval.
5. Notifications alert staff to new vendor responses.

## Information Required

- Vendor contacts and portal login accounts
- Messages, notes and attachments
- PO and delivery status
- Permission rules for what vendors can see and edit

## Output / Action

- A searchable communication history per vendor
- Order confirmations and delivery date updates
- Notifications and reminders for staff

## Benefits

- A clear record that resolves "who said what" disputes
- Faster order confirmation with less back-and-forth
- Vendors maintain their own details, reducing admin work

## Limitations / Dependencies

- Vendors must agree to use the portal
- Portal access needs strict permissions so vendors see only their own data
- Requires secure login and basic internet access on the vendor side
