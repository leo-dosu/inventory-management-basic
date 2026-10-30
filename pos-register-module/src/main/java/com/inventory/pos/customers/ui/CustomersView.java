package com.inventory.pos.customers.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.customers.service.CustomerAccount;
import com.inventory.pos.customers.service.CustomerService;
import com.inventory.pos.debt.service.ReminderService;
import com.inventory.pos.debt.ui.ReminderDialog;
import com.inventory.pos.debt.ui.StatementDownloads;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.inventory.pos.shell.MainLayout;
import com.inventory.pos.shell.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@StyleSheet("styles/customers.css")
@Route(value = Routes.CUSTOMERS, layout = MainLayout.class)
@PageTitle("Customers | Inventory")
public class CustomersView extends Div {

    private final CustomerService customers;
    private final ReminderService reminders;
    private final StatementDownloads pdf;
    private final Div cardGrid = new Div();
    private final Span count = new Span();
    private String search = "";

    public CustomersView(CustomerService customers, ReminderService reminders, StatementDownloads pdf) {
        this.customers = customers;
        this.reminders = reminders;
        this.pdf = pdf;
        addClassNames("page", "customers-page");

        Button add = Panels.button("Add New Customer", VaadinIcon.PLUS, "btn-primary");
        add.addClickListener(e -> CustomerDialog.open(customers, created -> refresh()));
        add(Panels.pageHeading("Customer Accounts & Credit Directory",
                "Manage credit limits, client contacts, and generate debtor statements", add));

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search customers by name, phone, email...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setClearButtonVisible(true);
        searchField.addClassName("search-field");
        searchField.addValueChangeListener(e -> {
            search = e.getValue();
            refresh();
        });
        count.addClassName("toolbar-count");
        Div toolbar = new Div(searchField, count);
        toolbar.addClassNames("card", "toolbar", "toolbar-split");

        cardGrid.addClassName("customer-grid");
        add(toolbar, cardGrid);
        refresh();
    }

    private void refresh() {
        List<CustomerAccount> accounts = customers.accounts(search);
        count.setText(Format.plural(accounts.size(), "client account", "client accounts"));
        cardGrid.removeAll();
        if (accounts.isEmpty()) {
            Div empty = Panels.emptyState(VaadinIcon.USERS, "No customers found",
                    "Add a customer, or try a different search.");
            empty.addClassName("span-all");
            cardGrid.add(empty);
            return;
        }
        accounts.forEach(account -> cardGrid.add(card(account)));
    }

    private Div card(CustomerAccount account) {
        Span name = new Span(account.name());
        name.addClassName("customer-name");
        Span status = account.owes()
                ? Panels.badge("Debt: " + Format.money(account.debt()), "debt")
                : Panels.badge("Settled · Zero Balance", "success");
        Div header = new Div(name, status);
        header.addClassName("customer-header");

        Div card = new Div(header);
        card.addClassName("customer-card");
        card.getClassNames().set("owes", account.owes());
        addContact(card, VaadinIcon.PHONE, account.phone());
        addContact(card, VaadinIcon.ENVELOPE_O, account.email());
        if (account.address() != null) {
            Span address = new Span(account.address());
            address.addClassName("customer-address");
            card.add(address);
        }
        if (account.notes() != null) {
            Span notes = new Span(account.notes());
            notes.addClassName("customer-notes");
            card.add(notes);
        }

        Span summary = new Span("Total Orders: " + account.orders() + "  ·  Spend: "
                + Format.money(account.spend()));
        summary.addClassName("customer-summary");
        Div actions = new Div();
        actions.addClassName("row-actions");
        if (account.orders() > 0) {
            Button statement = Panels.button("PDF", VaadinIcon.FILE_TEXT_O, "btn-statement");
            actions.add(pdf.statement(account.id(), account.name(), statement));
        }
        if (account.owes()) {
            Button notice = Panels.button("Notice", VaadinIcon.COMMENT_O, "btn-secondary");
            notice.addClickListener(e -> sendNotice(account));
            actions.add(notice);
        }
        Div footer = new Div(summary, actions);
        footer.addClassName("customer-footer");
        card.add(footer);
        return card;
    }

    private static void addContact(Div card, VaadinIcon icon, String value) {
        if (value == null) {
            return;
        }
        Component line = new Div(new Icon(icon), new Span(value));
        line.getElement().getClassList().add("customer-contact");
        card.add(line);
    }

    private void sendNotice(CustomerAccount account) {
        try {
            ReminderDialog.open(account.name(), reminders.prepareReminder(account.id()));
        } catch (BusinessException ex) {
            Notify.error(ex.getMessage());
        }
    }
}
