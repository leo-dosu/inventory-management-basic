# Feature Updates: POS System

**Name:** Awoniyi Nasrulahi  
**Matric No:** F/ND/25/3210240


 # 1. Vendor Invoice Verification & Three-Way Matching

## Description

Checks every vendor invoice against the purchase order and the goods receipt before it can be approved for payment. Quantity, price and item details must agree within an allowed tolerance.

## Purpose

Vendors can bill for goods that were not delivered, at the wrong price, or twice. Paying without checking leads to overpayments, duplicate payments and fraud risk.

## How It Works

1. The vendor invoice is entered or uploaded and linked to its PO.
2. The system retrieves the PO (what was ordered) and the goods receipt (what arrived).
3. It compares quantity, unit price and total across the three documents.
4. If everything matches within tolerance, the invoice is marked ready for approval.
5. Mismatches are flagged to a reviewer, who can query the vendor, correct a record, or reject the invoice.

## Information Required

- Vendor invoice (number, date, lines, totals, tax)
- Linked purchase order
- Linked goods receipt note
- Tolerance rules (for price and quantity)
- Reviewer and approver roles

## Output / Action

- Match status (matched, mismatched, on hold)
- Discrepancy list for review
- Invoice approved for payment or rejected/queried

## Benefits

- Prevents paying for undelivered or mispriced goods
- Detects duplicate invoices
- Creates an audit trail that supports vendor disputes

## Limitations / Dependencies

- Needs a PO and a goods receipt for every purchase to work properly
- Services without physical receipt may use a simpler two-way match
- Tolerance settings must be set sensibly to avoid too many false alerts


 # 2. Vendor Performance Scorecards

## Description

Measures and ranks suppliers using recorded results such as on-time delivery, order accuracy, product quality, pricing consistency and responsiveness, and presents them as a score or rating per vendor.

## Purpose

Without data, supplier decisions depend on memory and opinion. Scorecards show which vendors are reliable and which cause delays, shortages or quality problems, so the business can reward or replace them.

## How It Works

1. Performance data is collected from existing records: delivery dates against PO dates, received quantities, rejected/defective items, invoice mismatches and returns.
2. Staff can also add a rating after each order.
3. Each measure is weighted and combined into an overall vendor score.
4. Scores are tracked over time to show improvement or decline.
5. Vendors can be compared side by side or grouped into tiers.

## Information Required

- Promised and actual delivery dates
- Ordered vs received quantities
- Quality/defect and return records
- Invoice discrepancy history
- Staff ratings and scoring weights

## Output / Action

- Score and rating per vendor
- Trend charts and vendor comparison reports
- Flags for underperforming suppliers

## Benefits

- Evidence-based supplier selection and negotiation
- Early warning about unreliable suppliers
- Encourages vendors to improve through visible targets

## Limitations / Dependencies

- Depends on complete data from purchase orders, receiving and returns
- Weightings reflect business priorities and need review
- A new vendor has too little history for a reliable score

