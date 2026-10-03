#FEATURES UPDATES: POS and Vendor Relation System
Name: Salami Benjamin Boluwatife
Matric: F/ND/25/3210285

# 1. Purchase Order Creation & Tracking

## Description

Lets authorised staff create formal purchase orders (POs) to vendors, send them, get them approved, and track their status from draft to closed.

## Purpose

Verbal or informal orders lead to disputes about price, quantity and delivery dates. A PO is the written record of what the business agreed to buy and is the base document for receiving and paying.

## How It Works

1. A purchasing officer selects a vendor and adds products, quantities and agreed prices.
2. Vendor payment terms and delivery details are filled in automatically from the vendor record.
3. Orders above a set value go through an approval step.
4. The approved PO is sent to the vendor (email or portal), and its status is tracked (sent, confirmed, partially received, closed, cancelled).
5. The PO is later linked to goods receipts and invoices.

## Information Required

- Vendor record and payment terms
- Product list with quantities and agreed cost prices
- Expected delivery date and delivery location
- Approval limits and approver roles
- PO number sequence

## Output / Action

- A formal PO document sent to the vendor
- Status tracking and open-order list
- Committed spend figures for budget visibility

## Benefits

- Clear written agreement for each order
- Visibility of what is on order and when it should arrive
- Approval control prevents unauthorised purchases

## Limitations / Dependencies

- Depends on accurate vendor and product-cost data
- Needs defined approval rules and user roles
- Deciding what to order is done by staff (or another module); this feature records and tracks the order

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




