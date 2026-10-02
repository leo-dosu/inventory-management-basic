 POS and Vendor Relations — Features (Abraham)
 matric no:F/ND/25/3210232

# 1. Vendor Payables & Payment Tracking

## Description

Tracks what the business owes each vendor, when each payment is due, and what has been paid. It records payments, partial payments, credits and the running balance per vendor.

## Purpose

Late payments damage supplier relationships and can cost the business discounts or credit terms, while early or duplicate payments hurt cash flow. A clear payables record keeps payments timely and accurate.

## How It Works

1. Approved invoices create payable entries with due dates based on the vendor's payment terms.
2. The system lists upcoming and overdue payables.
3. A user selects invoices to pay and records the payment method, date and reference.
4. Partial payments and vendor credits (for example, from returns) are applied against the balance.
5. The vendor's account balance and payment history update automatically.

## Information Required

- Approved invoices and due dates
- Vendor payment terms and bank/payment details
- Payment method, amount, date and reference
- Vendor credit notes
- Payment approver roles

## Output / Action

- Due and overdue payables list
- Recorded payments and updated vendor balances
- Vendor statements and payment history
- Data for cash-flow and spend reports

## Benefits

- Fewer late or missed payments
- Clear view of money owed to each supplier
- Supports better negotiation with a record of reliable payment

## Limitations / Dependencies

- Depends on approved invoices from the verification step
- Actual bank transfers may happen outside the system unless a payment integration exists
- Payment access should be limited to authorised staff

# 2. Vendor Profile & Master Database

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

