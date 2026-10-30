# How to run the POS project (Groups 6 and 7)

This is one folder, `inventory-pos`. It is the whole app: screens and database together. There is nothing else to start.

## 1. Check Java (nothing to install if this is fine)

Open a **new** terminal and run:

```
java -version
```

It should say **25 or newer**. The project is built for Java 25, which is the version the lecturer specified.
You do not need to install Maven, MySQL or Node. Maven comes with the project (the `mvnw` file).
You need internet on the first run, because it downloads libraries (a few hundred MB). Port **8082** must be free.

## 2. Start it

Open a terminal **inside the `inventory-pos` folder** and run:

| System | Command |
|---|---|
| Windows (PowerShell or cmd) | `mvnw.cmd spring-boot:run` |
| Windows (Git Bash), Mac, Linux | `./mvnw spring-boot:run` |

The first start takes **1 to 3 minutes** while it downloads and builds. Wait until you see `Started PosApplication`.
Later starts are much faster. Leave the terminal open while you use the app.

## 3. Open it

Go to **http://localhost:8082**. There is no login. If the page is blank, wait a minute and refresh.

The database is in memory and is **rebuilt with demo data every time you start**, so changes you make are lost when you
stop it. That is intended for now.

## 4. Stop it

Press **Ctrl+C** in the terminal.

## What to try (about two minutes)

1. **POS Register**: click a few products, change a quantity, pick a customer from the dropdown, press **Proceed to
   Checkout**. Pay part of the total for a customer, and the rest becomes a debt. A walk-in must pay in full.
2. **Debt Recovery**: press **Record Payment** on a debtor. You review exactly which invoices the payment clears before
   you confirm. Also try **Statement (PDF)** and **Reminder**.
3. **Invoices**: filter by status, press **Pay**, open an invoice with the eye icon, download its PDF.
4. **Customers** and **Inventory**: add a customer, add or edit a product.

Screenshots of every screen are in `docs/screenshots/`.

## If something goes wrong

| Problem | Fix |
|---|---|
| "release version 25 not supported" or "invalid target release" | Your Java is older than 25. Update it, close the terminal, open a new one. |
| Odd build errors on a Java **newer** than 25 (for example 26 or 27) | Install Java 25 from https://adoptium.net next to it, then run from a new terminal with Java 25 selected. |
| "Port 8082 already in use" | Close whatever is using it, or restart the PC. |
| Page stays blank on first load | Wait a minute and refresh. It is building the first time. |
| Mac or Linux: "permission denied" | Run `chmod +x mvnw` once, then try again. |
| Internet is off | The first run needs it to download libraries. Connect and try again. |

## For people editing the code

Open the `inventory-pos` folder in VS Code (install the **Extension Pack for Java**). The code is split into feature
folders under `src/main/java/com/inventory/pos/` (`register`, `invoices`, `debt`, `customers`, `inventory` and more).
Each has `service/` for the business rules and `ui/` for the screens, and each has its own stylesheet in
`src/main/resources/META-INF/resources/styles/`. Run the tests with `mvnw test`.

Before you change anything, read `CONTRIBUTING.md` (the rules), pick your piece in `docs/WORK-PACKAGES.md`, and see
`docs/ARCHITECTURE.md` for how the parts fit together. The `README.md` explains what each screen does and what is not
built yet.
