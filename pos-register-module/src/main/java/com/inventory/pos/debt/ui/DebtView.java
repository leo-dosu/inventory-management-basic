package com.inventory.pos.debt.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.debt.service.DebtFilter;
import com.inventory.pos.debt.service.DebtOverview;
import com.inventory.pos.debt.service.DebtPaymentService;
import com.inventory.pos.debt.service.DebtService;
import com.inventory.pos.debt.service.Debtor;
import com.inventory.pos.debt.service.ReminderService;
import com.inventory.pos.invoices.service.InvoiceSummary;
import com.inventory.pos.invoices.ui.InvoiceDetailDialog;
import com.inventory.pos.shared.ui.FilterChips;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.inventory.pos.shell.MainLayout;
import com.inventory.pos.shell.Routes;
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
import java.util.Map;
import java.util.stream.Collectors;

/** Who owes money, how late it is, and the buttons to take a payment, print a statement or chase it. */
@StyleSheet("styles/debt.css")
@Route(value = Routes.DEBTS, layout = MainLayout.class)
@PageTitle("Debt Recovery | Inventory")
public class DebtView extends Div {

    private final DebtService debts;
    private final DebtPaymentService payments;
    private final ReminderService reminders;
    private final StatementDownloads pdf;
    private final Div cards = new Div();
    private final Div debtorList = new Div();
    private FilterChips<DebtFilter> chips;
    private Map<DebtFilter, Long> counts = Map.of();
    private String search = "";

    public DebtView(DebtService debts, DebtPaymentService payments, ReminderService reminders,
                    StatementDownloads pdf) {
        this.debts = debts;
        this.payments = payments;
        this.reminders = reminders;
        this.pdf = pdf;
        addClassNames("page", "debt-page");
        add(Panels.pageHeading("Debt Recovery & Credit Ledger",
                "Monitor unpaid balances, log partial recoveries, and issue formal PDF statements"));

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search debtor name, phone, invoice...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setClearButtonVisible(true);
        searchField.addClassName("search-field");
        searchField.addValueChangeListener(e -> {
            search = e.getValue();
            refreshList();
        });
        chips = new FilterChips<>(List.of(DebtFilter.values()), DebtFilter.ALL, this::chipLabel, DebtView::chipTone,
                selected -> refreshList());
        Div toolbar = new Div(searchField, chips);
        toolbar.addClassNames("card", "toolbar", "toolbar-split");

        cards.addClassNames("kpi-grid", "four");
        debtorList.addClassName("debtor-list");
        add(cards, toolbar, debtorList);
        reload();
    }

    private String chipLabel(DebtFilter filter) {
        return filter.label() + " (" + counts.getOrDefault(filter, 0L) + ")";
    }

    private static String chipTone(DebtFilter filter) {
        return switch (filter) {
            case OVERDUE -> "red";
            case DUE_SOON -> "amber";
            case ALL -> null;
        };
    }

    /** Refreshes everything: after a payment the cards, the counts and the list can all change. */
    private void reload() {
        DebtOverview overview = debts.overview();
        cards.removeAll();
        cards.add(
                statCard("TOTAL OUTSTANDING DEBT", Format.money(overview.outstanding()),
                        "Across " + Format.plural(overview.openInvoices(), "open invoice", "open invoices"), ""),
                statCard("OVERDUE ARREARS", Format.money(overview.overdueArrears()), "Past contracted due date", "red"),
                statCard("RECOVERED PAYMENTS", Format.money(overview.recovered()), "Collected via POS & bank", "green"),
                statCard("DEBTOR ACCOUNTS", String.valueOf(overview.debtorAccounts()),
                        "Clients with non-zero balances", ""));
        List<Debtor> everyone = debts.debtors(null, DebtFilter.ALL);
        counts = java.util.Arrays.stream(DebtFilter.values()).collect(Collectors.toMap(f -> f,
                f -> everyone.stream().filter(f::matches).count()));
        chips.refreshLabels();
        refreshList();
    }

    private void refreshList() {
        List<Debtor> shown = debts.debtors(search, chips.selected());
        debtorList.removeAll();
        if (shown.isEmpty()) {
            debtorList.add(Panels.emptyState(VaadinIcon.CHECK_CIRCLE, "No debtors here",
                    "Nobody matches this search and filter, or every account is settled."));
            return;
        }
        shown.forEach(debtor -> debtorList.add(row(debtor)));
    }

    private static Div statCard(String label, String value, String sub, String tone) {
        Span name = new Span(label);
        name.addClassName("stat-label");
        Span number = new Span(value);
        number.addClassNames("stat-value", "mono");
        Span note = new Span(sub);
        note.addClassName("stat-sub");
        Div card = new Div(name, number, note);
        card.addClassName("stat-card");
        if (!tone.isEmpty()) {
            card.addClassName(tone);
        }
        return card;
    }

    private Div row(Debtor debtor) {
        Span name = new Span(debtor.name());
        name.addClassName("debtor-name");
        Span tag = debtor.overdue()
                ? Panels.badge(debtor.maxDaysOverdue() + " days overdue", "debt")
                : Panels.badge("Active Terms", "warning");
        Div title = new Div(name, tag);
        title.addClassName("debtor-title");

        Div meta = new Div();
        meta.addClassName("debtor-meta");
        if (debtor.phone() != null) {
            meta.add(metaItem(VaadinIcon.PHONE, debtor.phone()));
        }
        if (debtor.email() != null) {
            meta.add(metaItem(VaadinIcon.ENVELOPE_O, debtor.email()));
        }
        meta.add(new Span(Format.plural(debtor.unpaidInvoices(), "unpaid invoice", "unpaid invoices")));
        Div who = new Div(title, meta);
        who.addClassName("debtor-who");

        Span balanceLabel = new Span("TOTAL DEBT BALANCE");
        balanceLabel.addClassName("stat-label");
        Span balance = new Span(Format.money(debtor.balance()));
        balance.addClassNames("debtor-balance", "mono");
        balance.getClassNames().set("text-danger", debtor.overdue());
        Div money = new Div(balanceLabel, balance);
        money.addClassName("debtor-money");

        Div details = new Div();
        details.addClassName("debtor-details");
        details.setVisible(false);

        Button pay = Panels.button("Record Payment", VaadinIcon.COIN_PILES, "btn-primary");
        pay.addClickListener(e -> DebtPaymentDialog.open(debtor, payments, () -> {
            MainLayout.refreshBadges();
            reload();
        }));
        Button statementButton = Panels.button("Statement (PDF)", VaadinIcon.FILE_TEXT_O, "btn-statement");
        Button reminder = Panels.button("Reminder", VaadinIcon.COMMENT_O, "btn-secondary");
        reminder.addClickListener(e -> remind(debtor));
        Button toggle = Panels.iconButton(VaadinIcon.ANGLE_DOWN, "Show unpaid invoices");
        toggle.addClickListener(e -> {
            boolean open = !details.isVisible();
            details.setVisible(open);
            toggle.setIcon(new Icon(open ? VaadinIcon.ANGLE_UP : VaadinIcon.ANGLE_DOWN));
            if (open) {
                fillDetails(details, debtor);
            }
        });
        Div actions = new Div(pay, pdf.statement(debtor.customerId(), debtor.name(), statementButton), reminder, toggle);
        actions.addClassName("row-actions");

        Div top = new Div(who, money, actions);
        top.addClassName("debtor-top");
        Div row = new Div(top, details);
        row.addClassName("debtor-row");
        row.getClassNames().set("overdue", debtor.overdue());
        return row;
    }

    private void fillDetails(Div details, Debtor debtor) {
        details.removeAll();
        for (InvoiceSummary invoice : debts.openInvoices(debtor.customerId())) {
            Span number = new Span(invoice.number());
            number.addClassNames("mono", "strong");
            Span due = new Span("Due " + Format.date(invoice.dueOn()));
            Span owed = new Span(Format.money(invoice.balance()));
            owed.addClassNames("mono", "strong");
            Div line = new Div(number, due, InvoiceDetailDialog.statusBadge(invoice.status()), owed);
            line.addClassName("debtor-invoice");
            details.add(line);
        }
    }

    private static Span metaItem(VaadinIcon icon, String text) {
        Span item = new Span(new Icon(icon), new Span(text));
        item.addClassName("meta-item");
        return item;
    }

    private void remind(Debtor debtor) {
        try {
            ReminderDialog.open(debtor.name(), reminders.prepareReminder(debtor.customerId()));
        } catch (BusinessException ex) {
            Notify.error(ex.getMessage());
        }
    }
}
