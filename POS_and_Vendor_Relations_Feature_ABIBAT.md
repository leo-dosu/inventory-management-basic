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


# 2. Discounts, Promotions & Pricing Rules

## Description

Lets the business define price reductions and offers (percentage or fixed discounts, buy-X-get-Y, bundle prices, time-limited promotions) and applies them at checkout under permission controls.

## Purpose

Promotions drive sales, but manual discounting leads to inconsistent prices, margin loss and staff abuse. This feature applies approved offers consistently and limits who can override prices.

## How It Works

1. A manager creates a promotion with its type, products or categories, start and end dates.
2. At checkout, eligible items trigger the rule automatically, or the cashier enters a manual discount within their allowed limit.
3. Discounts above the cashier's limit need manager approval.
4. The discount amount and reason are saved with the transaction.
5. The promotion expires automatically on its end date.

## Information Required

- Promotion rules, dates and eligible products/categories
- Product cost and minimum acceptable margin
- Discount limits per user role
- Coupon or voucher codes (if used)

## Output / Action

- Adjusted prices on the sale
- Logged discount amount and approving user
- Data for promotion performance and margin reports

## Benefits

- Consistent pricing across cashiers and locations
- Protects margins by capping discounts and requiring approvals
- Makes it possible to measure which promotions actually worked

## Limitations / Dependencies

- Needs cost data from the product catalog to protect margins
- Overlapping promotions need clear priority rules to avoid double discounts
- Vendor-funded promotions depend on agreed terms captured in vendor contracts

