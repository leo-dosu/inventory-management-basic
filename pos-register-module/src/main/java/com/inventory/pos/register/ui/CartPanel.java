package com.inventory.pos.register.ui;

import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.customers.service.CustomerAccount;
import com.inventory.pos.customers.service.CustomerService;
import com.inventory.pos.customers.ui.CustomerDialog;
import com.inventory.pos.invoices.service.InvoiceDetail;
import com.inventory.pos.invoices.ui.InvoiceDetailDialog;
import com.inventory.pos.invoices.ui.InvoiceDownloads;
import com.inventory.pos.register.service.CheckoutService;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.inventory.pos.shell.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** The right half of the register: who is buying, what is in the cart, the totals and the checkout button. */
public class CartPanel extends Div {

    private record Option(Long id, String name) {
    }

    private static final Option WALK_IN = new Option(null, "Walk-in Retail Guest (No Account)");

    private final CustomerService customers;
    private final CheckoutService checkout;
    private final InvoiceDownloads pdf;
    private final Runnable onSaleComplete;
    private final Cart cart = new Cart();
    private List<CustomerAccount> accounts = List.of();

    private final Div cartLines = new Div();
    private final Select<Option> customerSelect = new Select<>();
    private final BigDecimalField discount = new BigDecimalField();
    private final Span subtotalText = new Span();
    private final Span taxLabel = new Span();
    private final Span taxText = new Span();
    private final Span totalText = new Span();
    private final Button checkoutButton = Panels.button("Proceed to Checkout", null, "btn-primary");

    /** @param onSaleComplete called after a sale is saved, so the catalogue can show the new stock levels */
    public CartPanel(CustomerService customers, CheckoutService checkout, InvoiceDownloads pdf,
                     Runnable onSaleComplete) {
        this.customers = customers;
        this.checkout = checkout;
        this.pdf = pdf;
        this.onSaleComplete = onSaleComplete;
        addClassNames("card", "cart-panel");

        Span title = new Span("REGISTER CART");
        title.addClassName("cart-title");

        customerSelect.setWidthFull();
        customerSelect.setItemLabelGenerator(Option::name);
        Button addCustomer = Panels.iconButton(VaadinIcon.PLUS, "Add a customer");
        addCustomer.addClassName("outlined");
        addCustomer.addClickListener(e -> CustomerDialog.open(customers, created -> reloadCustomers(created.id())));
        Div customerRow = new Div(customerSelect, addCustomer);
        customerRow.addClassName("customer-row");

        Div header = new Div(title, customerRow);
        header.addClassName("cart-header");

        cartLines.addClassName("cart-lines");

        discount.setPrefixComponent(new Span("₦"));
        discount.setValue(BigDecimal.ZERO);
        discount.setValueChangeMode(ValueChangeMode.EAGER);
        discount.addClassName("discount-field");
        discount.addValueChangeListener(e -> refreshTotals());

        Div totals = new Div(
                totalRow(new Span("Subtotal:"), subtotalText),
                totalRow(new Span("Discount:"), discount),
                totalRow(taxLabel, taxText));
        totals.addClassName("totals");
        Div due = totalRow(new Span("Total Due:"), totalText);
        due.addClassName("total-due");

        checkoutButton.addClassName("checkout-button");
        checkoutButton.addClickListener(e -> startCheckout());

        Div footer = new Div(totals, due, checkoutButton);
        footer.addClassName("cart-footer");

        add(header, cartLines, footer);
        reloadCustomers(null);
        refreshCart();
    }

    /** Puts one of the product in the cart, or explains why it cannot be added. */
    public void addProduct(Product product) {
        if (!cart.add(product)) {
            Notify.error(product.getStock() < 1 ? product.getName() + " is out of stock."
                    : "Only " + product.getStock() + " of " + product.getName() + " in stock.");
        }
        refreshCart();
    }

    /** Puts a one-off line (such as a delivery fee) in the cart. */
    public void addCustom(String description, BigDecimal price, int quantity) {
        cart.addCustom(description, price, quantity);
        refreshCart();
    }

    private static Div totalRow(Component label, Component value) {
        Div row = new Div(label, value);
        row.addClassName("total-row");
        return row;
    }

    private void reloadCustomers(Long selectId) {
        accounts = customers.accounts(null);
        List<Option> options = new ArrayList<>();
        options.add(WALK_IN);
        accounts.forEach(a -> options.add(new Option(a.id(), a.name())));
        customerSelect.setItems(options);
        customerSelect.setValue(options.stream().filter(o -> selectId != null && selectId.equals(o.id()))
                .findFirst().orElse(WALK_IN));
    }

    private CustomerAccount selectedCustomer() {
        Option option = customerSelect.getValue();
        if (option == null || option.id() == null) {
            return null;
        }
        return accounts.stream().filter(a -> a.id().equals(option.id())).findFirst().orElse(null);
    }

    private void refreshCart() {
        cartLines.removeAll();
        if (cart.isEmpty()) {
            cartLines.add(Panels.emptyState(VaadinIcon.CLIPBOARD_TEXT, "Register is empty",
                    "Select items from catalog or add a custom item"));
        } else {
            cart.entries().forEach(entry -> cartLines.add(cartLine(entry)));
        }
        refreshTotals();
    }

    private Div cartLine(Cart.Entry entry) {
        Span name = new Span(entry.name);
        name.addClassName("cart-item-name");
        Span meta = new Span((entry.isCustom() ? "Custom item" : entry.sku) + "  ·  "
                + Format.money(entry.unitPrice));
        meta.addClassName("cart-item-meta");
        Div info = new Div(name, meta);
        info.addClassName("cart-item-info");

        Button minus = Panels.iconButton(VaadinIcon.MINUS, "One less");
        minus.addClassName("stepper");
        minus.addClickListener(e -> {
            cart.decrement(entry);
            refreshCart();
        });
        Span quantity = new Span(String.valueOf(entry.quantity));
        quantity.addClassName("stepper-value");
        Button plus = Panels.iconButton(VaadinIcon.PLUS, "One more");
        plus.addClassName("stepper");
        plus.addClickListener(e -> {
            if (!cart.increment(entry)) {
                Notify.error("Only " + entry.max + " of " + entry.name + " in stock.");
            }
            refreshCart();
        });
        Div stepper = new Div(minus, quantity, plus);
        stepper.addClassName("stepper-group");

        Span total = new Span(Format.money(entry.lineTotal()));
        total.addClassName("cart-item-total");
        Button remove = Panels.iconButton(VaadinIcon.CLOSE_SMALL, "Remove");
        remove.addClickListener(e -> {
            cart.remove(entry);
            refreshCart();
        });

        Div line = new Div(info, stepper, total, remove);
        line.addClassName("cart-line");
        return line;
    }

    private BigDecimal enteredDiscount() {
        return discount.getValue() == null ? BigDecimal.ZERO : discount.getValue();
    }

    private boolean discountInvalid(BigDecimal subtotal) {
        BigDecimal entered = enteredDiscount();
        return entered.signum() < 0 || entered.compareTo(subtotal) > 0;
    }

    private BigDecimal effectiveDiscount(BigDecimal subtotal) {
        return discountInvalid(subtotal) ? BigDecimal.ZERO : enteredDiscount();
    }

    private BigDecimal total(BigDecimal subtotal) {
        BigDecimal taxable = subtotal.subtract(effectiveDiscount(subtotal));
        return taxable.add(Invoice.taxOn(taxable, checkout.taxRate()));
    }

    private void refreshTotals() {
        BigDecimal subtotal = cart.subtotal();
        boolean invalid = discountInvalid(subtotal);
        discount.setInvalid(invalid);
        discount.setErrorMessage("Cannot exceed the subtotal");

        BigDecimal taxable = subtotal.subtract(effectiveDiscount(subtotal));
        BigDecimal tax = Invoice.taxOn(taxable, checkout.taxRate());
        String rate = checkout.taxRate().multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString();

        subtotalText.setText(Format.money(subtotal));
        taxLabel.setText("Tax (" + rate + "%):");
        taxText.setText(Format.money(tax));
        totalText.setText(Format.money(taxable.add(tax)));
        checkoutButton.setText("Proceed to Checkout  (" + Format.money(taxable.add(tax)) + ")");
        checkoutButton.setEnabled(!cart.isEmpty() && !invalid);
    }

    private void startCheckout() {
        BigDecimal subtotal = cart.subtotal();
        CheckoutDialog.open(checkout, selectedCustomer(), cart.lines(), effectiveDiscount(subtotal),
                total(subtotal), this::saleDone);
    }

    private void saleDone(InvoiceDetail sold) {
        cart.clear();
        discount.setValue(BigDecimal.ZERO);
        reloadCustomers(null);
        refreshCart();
        onSaleComplete.run();
        MainLayout.refreshBadges();
        Notify.success("Sale complete: " + sold.summary().number());
        InvoiceDetailDialog.open(sold, pdf);
    }
}
