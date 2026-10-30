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






---

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


---

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



---

# Feature Updates: POS System

**Name:** Adebayo Adebimpe Latifat  
**Matric No:** F/ND/25/3210362  

# 1. Sales Reporting & Analytics

## Description

Turns recorded transactions into reports and dashboards: sales by period, product, category, cashier, payment method and location, plus best- and worst-selling items and stock status.

## Purpose

Owners and managers need evidence to decide what to stock, who to buy from, and how the business is performing. Raw transactions alone do not answer those questions.

## How It Works

1. All sales, returns, payments and stock movements are stored in one database.
2. The reporting module aggregates them by the chosen filters (date range, location, product, staff).
3. Standard reports are available instantly (daily sales, end-of-day, top sellers, stock valuation).
4. Managers can build custom reports and export them (CSV/PDF).
5. Results can be viewed on a dashboard from any authorised device.

## Information Required

- Transaction, payment and return history
- Product, category and cost data
- Staff and location data
- Inventory movement history
- Date and filter selections

## Output / Action

- Sales, profit and payment-method reports
- Product performance lists (best and worst sellers)
- Cashier and shift performance summaries
- Exportable files and dashboard charts

## Benefits

- Data-driven decisions on stock and supplier purchasing
- Quick end-of-day reconciliation
- Early warning when a product or location underperforms

## Limitations / Dependencies

- Reports are only as good as the underlying data
- Real-time dashboards need a working connection to the central database
- Vendor-related analytics also depend on purchase and invoice data from the vendor side

# 2. Customer Returns, Refunds & Receipt Management

## Description

Handles customers sending items back: looking up the original sale, processing a refund or exchange, deciding what happens to the returned item, and producing printed or digital receipts for all transactions.

## Purpose

Returns are a normal part of retail but a common source of fraud and stock errors. Without a controlled process, refunds are given without proof, stock is miscounted, and defective goods that should be claimed from the vendor are lost.

## How It Works

1. The cashier finds the original receipt or transaction by number, barcode or customer.
2. The system checks the return policy (time limit, condition) and may require manager approval.
3. The cashier records the reason and the item's condition.
4. The refund is issued to the original payment method, store credit, or as an exchange.
5. The item is put back in stock, or marked damaged/defective; defective items can be flagged for a return to the vendor (see *Vendor Returns & Claims*).
6. A refund receipt is printed, emailed or sent by SMS.

## Information Required

- Original transaction record and receipt number
- Return policy rules and approval thresholds
- Return reason and item condition
- Original payment method
- Cashier and approving manager IDs

## Output / Action

- Refund or exchange transaction linked to the original sale
- Stock adjustment (restock or damaged stock)
- Flag/record for defective items to be claimed from the vendor
- Customer receipt (print, email or SMS)

## Benefits

- Reduces refund fraud through approval rules and linked records
- Keeps stock counts correct after returns
- Creates the evidence trail needed to claim credit from vendors for faulty goods

## Limitations / Dependencies

- Needs the original sale to be searchable
- Return rules must be defined by the business
- Vendor claim handling depends on the *Vendor Returns & Claims* feature




---

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






---

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



---

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



---

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



---

NAME:ADEYEMI BLESSING ADEDAMOLA
MATRIC NUMBER: F/ND/25/3210080

# 1. Customer Management & Loyalty

## Description

Stores customer profiles and purchase history, and supports loyalty rewards such as points, store credit and gift cards.

## Purpose

Repeat customers are cheaper to keep than new ones are to win. Without customer records, the business cannot recognise loyal buyers, reward them, or understand what they buy.

## How It Works

1. A customer is added at checkout (name, phone/email) or looked up by phone or card number.
2. The sale is linked to the customer's profile.
3. Loyalty points or store credit are earned per sale according to the programme rules.
4. Points or credit can be redeemed as a payment method at a later purchase.
5. Reports show top customers, visit frequency and spend.

## Information Required

- Customer name and contact details
- Purchase history linked to the profile
- Loyalty rules (points per amount, redemption value, expiry)
- Gift card and store-credit balances
- Customer consent for storing and contacting them

## Output / Action

- Customer profile with purchase history
- Points earned/redeemed and updated balances
- Customer-level reports (best customers, repeat rate)

## Benefits

- Encourages repeat purchases
- Helps identify the most valuable customers
- Supports targeted offers based on real purchase data

## Limitations / Dependencies

- Customer data must be handled in line with applicable data-protection rules
- Needs cashiers to capture customer details consistently
- Loyalty rules must be defined and monitored to prevent abuse

# 2. Real-Time Inventory Tracking

## Description

Keeps a live count of every product's stock. Stock goes down when items are sold and up when vendor deliveries are received, with support for multiple locations, stock counts and adjustments.

## Purpose

Selling an item that is not on the shelf, or not knowing what is in stock, hurts both customers and vendor purchasing decisions. Reports, loss prevention and vendor orders all rely on knowing the true stock level.

## How It Works

1. Each sale automatically deducts the sold quantity from the product's stock.
2. Each goods receipt from a vendor adds stock (see *Goods Receiving Against Purchase Orders*).
3. Staff can run stock counts and post adjustments with a reason (damage, theft, expiry).
4. Stock can be transferred between locations where the business has more than one.
5. The system flags items that fall below a minimum stock level set by the manager.

## Information Required

- Product records with SKUs and units of measure
- Opening stock quantities per location
- Sales transactions and goods receipts
- Stock count entries and adjustment reasons
- Minimum stock levels per product

## Output / Action

- Current on-hand quantity per item and per location
- Stock movement history (who changed what, and why)
- Low-stock flags for managers
- Variance reports from stock counts

## Benefits

- Fewer stock-outs and fewer sales of items that are not available
- Easier to spot shrinkage, damage and counting errors
- Gives the vendor side of the system accurate quantities for ordering and receiving

## Limitations / Dependencies

- Only as accurate as the data entered; unscanned sales or unrecorded deliveries cause drift
- Delayed syncing between terminals reduces the "real-time" value
- Automated purchasing decisions are outside this feature; it only reports stock and raises flags








---

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






---

