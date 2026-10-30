# Work packages

The project is cut into **52 work packages** so that about 40 people can each push their own piece to GitHub without
touching anyone else's files. Some people take two small ones. The leads take the shared ones (`SHELL`, `CORE`, `QA`).

**Path shortcuts used below**

| Short | Means |
|---|---|
| `pos/` | `src/main/java/com/inventory/pos/` |
| `test/` | `src/test/java/com/inventory/pos/` |
| `css/` | `src/main/resources/META-INF/resources/styles/` |

**Preview** 

| Who | Packages | Count |
|---|---|---|
| Group 6 (the POS side) | `REG`, `INV`, `CUS`, `CRET`, `REP`, `LOY` | 22 |
| Group 7 (stock, vendors, debts) | `DEBT`, `STK`, `VEN`, `COM`, `PO`, `PAY`, `VRET`, `CON` | 20 |
| Leads (both groups) | `SHELL`, `CORE`, `QA` | 10 |


## Existing screens

### SHELL: the frame and shared parts

| ID | Piece | Files I own | What i did | Needs First | Owner |
|---|---|---|---|---|---|
| SHELL-1 | Shell frame and sidebar | `pos/shell/MainLayout.java`, `NavEntry.java`, `Routes.java` | Made the sidebar slide in on phones. Keep the rule that a screen joins the menu by adding a `NavEntry`, never by editing this class. | | Israel Usman (F/ND/25/3210261) |
| SHELL-2 | Shared UI toolkit | `pos/shared/ui/*` (`Format`, `Notify`, `Panels`, `FilterChips`, `Forms`, `Downloads`) | Wrote tests for `Format`. Added a short usage example to each class's Javadoc. | | Israel Usman (F/ND/25/3210261) |
| SHELL-3 | Base styling | `css/base.css` | Compared every screen with the Figma frames and fix spacing and colours. Check text contrast. | | Kpokonu David Odunayo (F/ND/25/3210048) |
| SHELL-4 | PDF toolkit | `pos/shared/pdf/Pdf.java` | Embeded a font with the naira sign so PDFs print ₦ instead of "NGN". | | Kpokonu David Odunayo (F/ND/25/3210048) |

### CORE: the data everyone shares

| ID | Piece | Files i own | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| CORE-1 | Product and Customer entities | `pos/core/domain/Product.java`, `Customer.java` | Added validation (name required, price at least 0) with tests in `test/core/domain/`. | | ODDE OPEYEMI SAMUEL (F/ND/25/3210122) |
| CORE-2 | Invoice, lines, payments | `pos/core/domain/Invoice.java`, `InvoiceLine.java`, `Payment.java`, `PaymentMethod.java`, `InvoiceStatus.java` | Wrote `InvoiceTest`: tax rounding, status rules (paid, partial, unpaid, overdue). | | ODDE OPEYEMI SAMUEL (F/ND/25/3210122) |
| CORE-3 | Repositories and Reminder | `pos/core/repository/*`, `pos/core/domain/Reminder.java` | Wrote a `@DataJpaTest` for each custom query. | | ODDE OPEYEMI SAMUEL (F/ND/25/3210122) |
| CORE-4 | Demo data | `pos/demo/DemoDataSeeder.java` | Added 20 more products and 6 more customers. Kept the five invoices' story (overdue, partly paid, paid, walk-in) intact. | | Oladele David Oluwaseun (F/ND/25/3210356) |

### REG: POS Register (Group 6)

| ID | Piece | Files i own | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| REG-1 | Checkout rules | `pos/register/service/CheckoutService.java`, `CheckoutRequest.java`, `CartLine.java`, `test/register/service/CheckoutServiceTest.java` | Added a test for any rule not yet covered. Then added the backend for a sale paid by several methods (split tender) together with REG-5. | | Israel Usman (F/ND/25/3210261) |
| REG-2 | Cart state | `pos/register/ui/Cart.java`, new `test/register/ui/CartTest.java` | Unit-test add, decrement and the stock limit. Then add **hold and resume a sale** (spec: holding, saving and merging open orders). | | Salami Benjamin Boluwatife (F/ND/25/3210285) |
| REG-3 | Product catalogue | `pos/register/ui/ProductCatalogue.java` | Typing an exact SKU and pressing Enter adds that product (barcode scanners type like this). Added a "low stock" tag. | | Salami Benjamin Boluwatife (F/ND/25/3210285) |
| REG-4 | Cart panel | `pos/register/ui/CartPanel.java` | Added a "clear cart" button with confirmation. Kept the totals exactly in step with the invoice. | | Abibat Foladara Omosanya (F/ND/25/3210213) |
| REG-5 | Checkout dialog | `pos/register/ui/CheckoutDialog.java` | Lets the cashier add more than one payment (cash plus card) until the total is covered. Needs REG-1's backend. | REG-1 | Saadu Quadri Bolaji (F/ND/25/3210151) |
| REG-6 | Custom item dialog | `pos/register/ui/CustomItemDialog.java` | Better validation messages, a maximum length, and remember the last description. | | Uche Gift Ebube (F/ND/25/3210114) |
| REG-7 | Register styling | `css/register.css` | A tablet layout (768 px wide) and bigger touch targets. | | Uche Gift Ebube (F/ND/25/3210114) |

### INV: Invoices (Group 6)

| ID | Piece | Files i own | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| INV-1 | Invoice service | `pos/invoices/service/InvoiceService.java`, `InvoiceFilter.java`, `InvoiceSummary.java`, `InvoiceDetail.java`, `test/invoices/service/InvoiceServiceTest.java` | Added a date-range filter to `list(...)`, with tests. | | Ogundare Omoyemi Victoria (F/nd/25/3210233) |
| INV-2 | Invoices screen | `pos/invoices/ui/InvoicesView.java` | Sortable columns, and kept the chosen filter when the page reloads. | | Williams Fadejimi Eniola (F/ND/25/3210016) |
| INV-3 | Payment dialog | `pos/invoices/ui/PaymentDialog.java` | Quick buttons "Pay full balance" and "Pay half". Clearer error messages. | | Williams Fadejimi Eniola (F/ND/25/3210016) |
| INV-4 | Invoice detail dialog | `pos/invoices/ui/InvoiceDetailDialog.java` | A Print button that uses the browser's print layout. | | Isaac Daniel Ukwuoma (F/ND/25/3210281) |
| INV-5 | Invoice PDF | `pos/invoices/pdf/InvoicePdf.java`, `pos/invoices/ui/InvoiceDownloads.java`, `test/invoices/pdf/InvoicePdfTest.java` | Added the business name block with address, and page numbers in the footer. | | Adeniyi Omoseeke Inioluwa (F/ND/25/3210001) |
| INV-6 | Export and styling | `css/invoices.css`, new `pos/invoices/ui/InvoiceCsv.java` | An "Export CSV" button for the filtered list. | INV-1 | Lawal AbdulBasit Ayinde (F/ND/25/3210206) |

### DEBT: Debt Recovery (Group 7)

| ID | Piece | Files i own | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| DEBT-1 | Debt summary and debtor list | `pos/debt/service/DebtService.java`, `DebtOverview.java`, `Debtor.java`, `DebtFilter.java`, `test/debt/service/DebtServiceTest.java` | Added a sort option (by balance, by days overdue) to `debtors(...)`, with tests. | | Olaoluwa Enoch Toluwalase (F/ND/25/3210154) |
| DEBT-2 | Taking debt payments | `pos/debt/service/DebtPaymentService.java`, `PaymentAllocation.java`, `DebtPaymentResult.java`, `test/debt/service/DebtPaymentServiceTest.java` | Lets the caller choose "oldest first" or "pay this invoice". Test both. | | ODDE OPEYEMI SAMUEL (F/ND/25/3210122) |
| DEBT-3 | Reminders | `pos/debt/service/ReminderService.java`, `pos/debt/ui/ReminderDialog.java`, `test/debt/service/ReminderServiceTest.java` | Show the history of reminders already prepared for a customer. | | Churchill Samuel Kelechi (F/ND/25/3210263) |
| DEBT-4 | Debt screen | `pos/debt/ui/DebtView.java` | Added a "Promise to pay by" date on a debtor and show it on the row (agree the field with CORE-3). | | Yekini Abdulrahmon Akorede (F/ND/25/3210076) |
| DEBT-5 | Payment confirmation dialog | `pos/debt/ui/DebtPaymentDialog.java` | After confirming, show a receipt summary the cashier can read out or print. | | Idama Daniel Oghenekparoboh (F/ND/25/3210317) |
| DEBT-6 | Statements | `pos/debt/service/StatementService.java`, `Statement.java`, `pos/debt/pdf/StatementPdf.java`, `pos/debt/ui/StatementDownloads.java`, `test/debt/service/StatementServiceTest.java`, `test/debt/pdf/StatementPdfTest.java` | Added a "statement from / to" date range. | | Akukwe Virginia Chizoba (F/ND/25/3210284) |
| DEBT-7 | Debt styling and menu entry | `css/debt.css`, `pos/debt/ui/DebtNav.java` | Made the debtor rows readable on a phone. | | Oyebode Abdulganiu Adebola (F/ND/25/3210194) |

### CUS: Customers (Group 6)

| ID | Piece | Files i own | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| CUS-1 | Customer service | `pos/customers/service/CustomerService.java`, `CustomerForm.java`, `CustomerAccount.java`, `test/customers/service/CustomerServiceTest.java` | Added `update(id, form)` with tests (name required, terms 0 to 365). | | Yusuf Anifat Titilope (F/ND/25/3210110) |
| CUS-2 | Customers screen | `pos/customers/ui/CustomersView.java`, `CustomersNav.java`, `css/customers.css` | An Edit button on each card. Sort by name or by debt. | CUS-1, CUS-3 | Afolabi Ayomide Praise (F/ND/25/3210166) |
| CUS-3 | Customer dialog | `pos/customers/ui/CustomerDialog.java` | Made the dialog work for editing as well as adding (copy how `ProductDialog` does it). | CUS-1 | Afolabi Ayomide Praise (F/ND/25/3210166) |
| CUS-4 | Purchase history | new `pos/customers/service/CustomerHistoryService.java`, new `pos/customers/ui/CustomerHistoryDialog.java` | A "History" button on a card listing that customer's invoices. | | Adebayo Adebimpe Latifat (F/ND/25/3210362) |

### STK: Inventory (Group 7)

| ID | Piece | Files i own | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| STK-1 | Product service | `pos/inventory/service/ProductService.java`, `ProductForm.java`, `test/inventory/service/ProductServiceTest.java` | Added `lowStock(threshold)` with tests. | | Oladele David Oluwaseun (F/ND/25/3210356) |
| STK-2 | Inventory screen | `pos/inventory/ui/InventoryView.java`, `InventoryNav.java`, `css/inventory.css` | Highlight low-stock rows, and added a "low stock only" chip. | STK-1 | OLUWASEGUN EMMANUEL TOLUWANI (F/ND/25/321301) |
| STK-3 | Product dialog | `pos/inventory/ui/ProductDialog.java` | Warning before saving a price of zero. Shows which invoices used the product. | | Odeyemi Joseph Ayomide (F/ND/25/3210205) |
| STK-4 | Stock adjustments | new `pos/inventory/domain/StockMovement.java`, `pos/inventory/service/StockService.java`, `pos/inventory/ui/StockAdjustmentDialog.java` | Records every change to stock (sale, delivery, adjustment) in a log, with an "Adjust stock" dialog (spec: real-time inventory tracking). | | Salaudeen Abdul Malik Adeniyi (F/ND/25/3210332) |

---

## New features

Each of these starts as a folder with a `README.md` that says what to build and how it plugs in. They create **new
files only**; nothing in the existing screens needs editing to add them. The rules about which packages a feature may
use are enforced by `ArchitectureTest`.

### Group 7: vendors and purchasing

| ID | Piece | Folder | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| VEN-1 | Vendor data | `pos/vendors/domain/`, `pos/vendors/repository/` | The `Vendor`, `VendorContact` and `VendorProduct` entities and repositories. **Push this first**, because PO, payables, contracts and comms all need it. | | Oyewo Omotolani Emmanuel (F/ND/25/3210191) |
| VEN-2 | Vendor service | `pos/vendors/service/`, `test/vendors/` | Add, edit, deactivate and search vendors, with tests. | VEN-1 | Alaran Aishat Ololade (F/ND/25/3210293) |
| VEN-3 | Vendors screen | `pos/vendors/ui/`, `css/vendors.css` | A vendor list with a status filter and an add and edit dialog, plus its `VendorsNav` (order 60). | VEN-2 | George Godglory Praise (F/ND/25/3210058) |
| COM-1 | Communication log | `pos/vendorcomms/` | Log a call, email or WhatsApp against a vendor and show the history. (Supplier portal is a stretch goal.) | VEN-1 | Akinyemi Oluwafemipaul (F/ND/25/3210111) |
| PO-1 | Purchase order service | `pos/purchasing/domain/`, `repository/`, `service/`, `test/purchasing/` | Create, send and **receive** purchase orders in parts. Receiving raises stock. | VEN-1 | Mathew Martha Theresa (F/ND/25/3210161) |
| PO-2 | Purchase order screens | `pos/purchasing/ui/`, `css/purchasing.css` | A PO list with status chips, a create dialog and a receive dialog, plus `PurchasingNav` (order 80). | PO-1 | Balogun Sekinat (F/ND/25/3210095) |
| PAY-1 | Vendor payables | `pos/payables/` | Bills from received POs, part payments and the balance per vendor. Copy the idea of the Debt screen. | VEN-1, PO-1 | Abdulrasheed Abdulquadri Temilade (F/ND/25/3210308) |
| VRET-1 | Vendor returns | `pos/vendorreturns/` | Raise a return against a PO. It lowers stock, and a credit lowers payables. | PO-1, PAY-1 | Adeshina Quadri Ayomide (F/ND/25/3210138) |
| CON-1 | Contracts and pricing | `pos/contracts/` | Contracts with agreed prices, and a method that returns the agreed price for a vendor and product on a date. | VEN-1 | Abdulsalam Mazeed Adedeji (F/ND/25/3210190) |

### Group 6: customers and reporting

| ID | Piece | Folder | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| CRET-1 | Customer returns service | `pos/customerreturns/domain/`, `repository/`, `service/`, `test/customerreturns/` | Return part of an invoice and refund or restock. Cannot return more than was sold. | | Ikonagbon Faith Osose (F/ND/25/3210005) |
| CRET-2 | Returns screens and receipts | `pos/customerreturns/ui/`, `css/customerreturns.css` | A "Return" dialog started from an invoice, and a receipt PDF for sales and refunds. | CRET-1 | Adeyemi Blessing Adedamola (F/ND/25/3210080) |
| REP-1 | Sales report service | `pos/salesreports/service/`, `test/salesreports/` | Totals by day, by product and by payment method for a date range. Read-only. | | Muhammad Adeyemi Olamilekan (F/ND/25/3210414) |
| REP-2 | Sales report screen | `pos/salesreports/ui/`, `css/salesreports.css` | A report page with a date range, tabs, and best and worst sellers, plus `SalesReportsNav` (order 130). | REP-1 | Obioma Ugochukwu Emmanuel (F/ND/25/3210351) |
| LOY-1 | Loyalty and store credit | `pos/loyalty/` | Points earned on paid sales and redeemed later. Agree the checkout hook with REG-5. | | Ogunware Abdulwasiu Olamide (F/ND/25/3210320) |

### QA: quality (leads)

| ID | Piece | Files i own | What i did | Needs first | Owner |
|---|---|---|---|---|---|
| QA-1 | End-to-end checks | new `test/e2e/` | A smoke test that starts the app and checks every screen's address answers. Run it in the pull request check. | | ODDE OPEYEMI SAMUEL (F/ND/25/3210122) |
| QA-2 | Demo and release | `docs/DEMO-SCRIPT.md`, the release tag | Wrote a five-minute demo script. Merge the finished pieces, run the full test suite and tag the release. | | Israel Usman (F/ND/25/3210261) |

---

