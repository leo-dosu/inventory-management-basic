*name* -- Obioma Ugochukwu Emmanuel
*matric number* -- F/ND/25/3210351
 
 
 # 1. Discounts, Promotions & Pricing Rules

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


# 2. Vendor Contract & Pricing Management

## Description

Stores vendor contracts and agreed terms (prices, discounts, payment terms, delivery commitments, validity dates) and links them to the vendor and to the products they cover.

## Purpose

Agreed terms are easily forgotten or applied inconsistently. Missed renewal dates and unnoticed price changes cost money and weaken the business's negotiating position.

## How It Works

1. A contract record is created for a vendor with start and end dates, terms and an uploaded copy of the agreement.
2. Agreed prices and discounts are saved against the relevant products.
3. When a PO is created, the contract price and payment terms are used by default.
4. The system sends reminders before a contract expires or needs renewal.
5. Price changes are logged so the business can see cost history per product.

## Information Required

- Contract documents and key terms
- Start, end and renewal dates
- Agreed prices, discounts and payment terms
- Linked vendor and products
- Contract owner

## Output / Action

- Contract terms applied to purchase orders
- Renewal and expiry alerts
- Cost price history per product and vendor

## Benefits

- Terms are actually followed on every order
- No surprise lapses in contracts
- Cost history supports negotiation and margin decisions

## Limitations / Dependencies

- Terms must be entered correctly and kept up to date
- Legal review of contracts happens outside the system
- Price changes should update the product catalog's cost field

