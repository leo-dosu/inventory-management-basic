# POS and Vendor Relations — Features (GROUP LEADER)
*name* -- ODDE OPEYEMI SAMUEL 
*matric number* -- F/ND/25/3210122

# Sales Checkout & Transaction Processing

## Description

The core selling screen of the POS. A cashier builds a sale by adding items and quantities, the system calculates line totals and tax, and the completed transaction is recorded with a unique ID. It also supports holding, saving, merging and splitting open orders.

## Purpose

Every other feature in the module depends on accurate sales data. Manual price lookups and hand-calculated totals cause long queues, pricing mistakes and sales that never reach the records. This feature makes each sale fast, correct and traceable.

## How It Works

1. The cashier opens a new sale on a registered terminal.
2. Items are added by scanning a barcode or searching the catalog.
3. The system pulls each price, applies tax, and updates the running total.
4. The cashier can hold the sale, merge it with another, or split it between customers.
5. Payment is taken (see *Multi-Payment & Split Tender*), and the transaction is saved and stock is reduced.

## Information Required

- Product catalog with prices and tax categories
- Tax rates and rules
- Cashier ID, register/terminal ID, and timestamp
- Optional customer reference
- Input from peripherals (barcode scanner, receipt printer, cash drawer)

## Output / Action

- A completed transaction record with a unique transaction number
- A receipt for the customer
- Automatic stock deduction for each item sold
- Entries in the sales ledger and daily totals

## Benefits

- Shorter queues and fewer cashier errors
- Every sale is recorded, so sales and stock figures can be trusted
- Hold/split/merge support lets staff handle busy periods and group payments

## Limitations / Dependencies

- Depends on an up-to-date product catalog and correct tax configuration
- Needs compatible hardware (scanner, printer, cash drawer) to be fully effective
- Needs a network connection or the offline mode feature to keep working during outages

# 2. Multi-Payment & Split Tender Processing

## Description

Lets the POS accept several payment methods (cash, debit/credit cards, mobile wallets, QR payments, gift cards, store credit) and allows one bill to be settled using a combination of them.

## Purpose

Customers expect to pay their own way. A POS that handles only one method, or cannot split a bill, loses sales and forces staff into manual workarounds. It also must keep payment data safe.

## How It Works

1. At payment, the cashier sees the amount due and chooses a method.
2. For a split, the cashier enters a partial amount for the first method; the remaining balance stays open.
3. Further payments are added until the balance reaches zero.
4. The system calculates change for cash and records each tender separately.
5. Card and wallet payments are sent to the payment processor and the response (approved/declined) is recorded.

## Information Required

- Amount due, tips, and amount tendered
- Accepted payment methods configured by the business
- Payment processor/gateway connection details
- Gift card or store-credit balances
- Card terminal or reader hardware

## Output / Action

- Payment confirmation (or decline) for each tender
- Change due calculation
- A payment breakdown attached to the transaction
- Data for end-of-day cash and card reconciliation

## Benefits

- Customers can pay however they prefer, including mixed methods
- Cleaner reconciliation because each tender is logged separately
- Fewer lost sales caused by unsupported payment types

## Limitations / Dependencies

- Needs an agreement with a payment processor, which usually charges fees
- Card and wallet payments require compatible reader hardware
- Payment data must be handled securely (for example, following card-industry security standards and not storing raw card details)

