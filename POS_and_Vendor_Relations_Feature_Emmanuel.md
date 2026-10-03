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

# 2. Vendor Returns & Claims Management

## Description

Manages sending defective, damaged, expired or wrongly delivered goods back to the vendor (return to vendor, RTV), and tracking the credit, replacement or refund the vendor owes in return.

## Purpose

Without a process, faulty goods sit in stock or are written off, and credit owed by suppliers is never collected. It also links POS returns to the vendor so supplier-caused losses are recovered.

## How It Works

1. Items are identified as returnable, either at goods receiving or from a customer return flagged as defective at the POS.
2. The reason is recorded, and the vendor's return authorisation (RMA) number is requested.
3. A return record is created listing items, quantities and reason; the stock is removed or moved to a separate returns location.
4. The goods are shipped back with the authorisation attached.
5. The vendor issues a credit note, replacement or refund, which is matched to the return and applied to the vendor's payable balance.
6. The return is closed when the credit or replacement is settled; unresolved claims stay on a follow-up list.

## Information Required

- Item, quantity, batch and reason for return
- Original PO, goods receipt, or POS sale reference
- Vendor return authorisation number and return terms
- Evidence (photos, notes)
- Vendor credit note or replacement details

## Output / Action

- Return record and stock adjustment
- Open claims list with ageing
- Vendor credit applied to payables
- Return and defect data feeding vendor scorecards

## Benefits

- Recovers money or goods owed by vendors
- Keeps defective stock from being resold or lost
- Gives objective data for evaluating supplier quality

## Limitations / Dependencies

- Return acceptance depends on each vendor's terms and time limits
- Needs accurate links to the original PO, receipt or sale
- Shipping costs and responsibility must be agreed with the vendor
