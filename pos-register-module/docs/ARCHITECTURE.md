# How the project is organised

One Spring Boot application with one database. Screens are Vaadin (Java) and talk to services directly. There is no
separate web API between them. The code is cut into **feature packages** so that each person works in their own folder.

## The package map

```
src/main/java/com/inventory/pos/
  PosApplication.java        starts the app (nobody edits this)

  core/                      the data everyone shares: entities, repositories, BusinessException
    domain/                    Product, Customer, Invoice, InvoiceLine, Payment, Reminder ...
    repository/                Spring Data repositories for those entities
  shared/                    building blocks every screen uses
    ui/                        Format, Notify, Panels, FilterChips, Forms, Downloads
    pdf/                       Pdf (fonts and table helpers)
  shell/                     the frame: MainLayout (header and sidebar), NavEntry, Routes

  register/                  POS Register         service/ + ui/
  invoices/                  Invoices             service/ + pdf/ + ui/
  debt/                      Debt Recovery        service/ + pdf/ + ui/
  customers/                 Customers            service/ + ui/
  inventory/                 Inventory            service/ + ui/

  vendors/ vendorcomms/ purchasing/ payables/ vendorreturns/ contracts/
  customerreturns/ salesreports/ loyalty/          not built yet: each has a README.md

  demo/                      demo data for the in-memory database
```

Each feature folder looks the same inside:

| Folder | What goes in it |
|---|---|
| `domain/`, `repository/` | The feature's own tables (only for features that have their own; the original five use `core`) |
| `service/` | The business rules, and the small records a screen reads. **No Vaadin code here.** |
| `pdf/` | PDF layouts, if the feature prints anything |
| `ui/` | Views, dialogs, and the feature's `...Nav` sidebar entry. **No database code here.** |

Tests mirror this under `src/test/java/com/inventory/pos/`. Styling is one file per feature in
`src/main/resources/META-INF/resources/styles/`.

## Who may import whom

```
            shell        shared        core
              \           |           /
               \          |          /
   inventory   invoices ──┴─ everything may use core, shared and shell
                  ▲
                  │
                debt
                  ▲
                  │
              customers          register ──► inventory, invoices, customers
```

In words: `core` imports nothing. `shared` may use `core`. Every feature may use `core`, `shared` and `shell`.
Beyond that, only these are allowed:

| Package | May also import |
|---|---|
| `debt` | `invoices` |
| `customers` | `debt` (for statements and reminders) |
| `register` | `inventory`, `invoices`, `customers` |
| `vendorcomms`, `purchasing`, `contracts` | `vendors` |
| `payables` | `vendors`, `purchasing` |
| `vendorreturns` | `vendors`, `purchasing`, `payables` |
| `customerreturns`, `salesreports` | `invoices` |
| `loyalty` | `customers` |

`ArchitectureTest` checks this on every `./mvnw test` and names the file that broke the rule. If you need something
from a package that is not on your list, ask a lead to move the shared part down into `core` or `shared`.

## Adding a screen (no shared file gets edited)

Say you are building the Vendors screen in `pos/vendors/`.

**1. The view**, in `pos/vendors/ui/VendorsView.java`:

```java
@StyleSheet("styles/vendors.css")
@Route(value = "vendors", layout = MainLayout.class)
@PageTitle("Vendors | Inventory")
public class VendorsView extends Div {
    public VendorsView(VendorService vendors) {
        addClassNames("page", "vendors-page");
        add(Panels.pageHeading("Vendors", "Suppliers we buy from"));
        // ... build the screen from Panels, FilterChips, Grid ...
    }
}
```

**2. The sidebar entry**, in `pos/vendors/ui/VendorsNav.java`. Copy `register/ui/RegisterNav.java`, change the label,
icon, target view, and give it a free `order()` (existing entries use 10 to 50). The shell finds it by itself.

**3. Your stylesheet**, `styles/vendors.css`, starting with `.vendors-page` on every selector (see below).

**4. Your service and tests**, extending `PosDataTest`. Copy `test/debt/service/DebtPaymentServiceTest.java`.

**5. One line in `ArchitectureTest`** only if your package is brand new and has no rule yet.

That is all. No edit to `MainLayout`, `Routes`, `base.css` or `pom.xml`.

## Styling rules

- **`base.css` is shared.** It holds the colours, the header and sidebar, buttons, chips, badges, grids and dialog rows. Only the leads edit it.
- **Your screen's rules go in your own file**, and every selector starts with your page's class:

  ```css
  .debt-page .stat-value { font-size: 26px; }      /* yes */
  .stat-value { font-size: 26px; }                 /* no: base.css can override it */
  ```

  The browser loads a screen's stylesheet *before* `base.css`, and when two rules tie, the later one wins. Starting
  every selector with your page class makes yours more specific, so it wins no matter the order. We found this out
  when the numbers on Debt Recovery shrank after the stylesheets were split.
- **Dialogs are the exception.** A dialog is drawn outside the page, so `.debt-page ...` would never match it. Give
  dialog-only classes unique names (`.allocation-row`, `.big-total`) and leave them unprefixed.
- Use the colour and spacing variables from `base.css` (`var(--ink)`, `var(--muted)`, `var(--radius)`), not new hex codes.

## Data and rules

- **Money is `BigDecimal`.** Show it with `Format.money(...)`. Never use `double` for money.
- **Services are `@Transactional`.** Reads use `readOnly = true`.
- **A view never touches a repository.** It calls a service, and gets back a record (like `InvoiceSummary`) or an entity with no lazy fields.
- **Why records:** a screen runs after the database session has closed, so reading a lazy field in a view crashes. A service turns what the screen needs into plain records inside the transaction.
- **Mistakes the user can fix** throw `BusinessException("a sentence a cashier can read")`. Screens show the message as written.
- **Invoice lines copy the product's name, SKU and price** at the time of sale, so editing a product never changes an old invoice.
- **"Today" comes from the `Clock` bean**, never `LocalDate.now()` directly, so tests can fix the date.

## Testing

- **Service tests** extend `PosDataTest`: H2, every service, a fixed clock (28 October 2026), a 10% tax rate, two products and a customer. Copy `test/debt/service/DebtPaymentServiceTest.java`.
- **PDF tests** build a record by hand and check the bytes start with `%PDF-`.
- **`ArchitectureTest`** guards the package rules above.
- **Screens** are checked by running the app (`./mvnw spring-boot:run`, http://localhost:8082) and clicking through. Attach a screenshot to your pull request.

## Naming

| Thing | Pattern | Example |
|---|---|---|
| Screen | `<Name>View` | `DebtView` |
| Pop-up | `<Name>Dialog` | `DebtPaymentDialog` |
| Menu entry | `<Name>Nav` | `DebtNav` |
| Business rules | `<Name>Service` | `DebtPaymentService` |
| Form input | `<Name>Form` | `ProductForm` |
| Data a screen reads | a record named for what it is | `Debtor`, `InvoiceSummary` |
| Page root CSS class | `<feature>-page` | `debt-page` |
