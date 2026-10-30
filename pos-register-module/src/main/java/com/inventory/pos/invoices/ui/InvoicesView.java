package com.inventory.pos.invoices.ui;

import com.inventory.pos.core.domain.InvoiceStatus;
import com.inventory.pos.invoices.service.InvoiceFilter;
import com.inventory.pos.invoices.service.InvoiceService;
import com.inventory.pos.invoices.service.InvoiceSummary;
import com.inventory.pos.shared.ui.FilterChips;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Panels;
import com.inventory.pos.shell.MainLayout;
import com.inventory.pos.shell.Routes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
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

@StyleSheet("styles/invoices.css")
@Route(value = Routes.INVOICES, layout = MainLayout.class)
@PageTitle("Invoices | Inventory")
public class InvoicesView extends Div {

    private final InvoiceService invoices;
    private final InvoiceDownloads pdf;
    private final Grid<InvoiceSummary> grid = new Grid<>();
    private final Div tableHolder = new Div();
    private FilterChips<InvoiceFilter> chips;
    private Map<InvoiceFilter, Long> counts = Map.of();
    private String search = "";

    public InvoicesView(InvoiceService invoices, InvoiceDownloads pdf) {
        this.invoices = invoices;
        this.pdf = pdf;
        addClassNames("page", "invoices-page");
        add(Panels.pageHeading("Invoices & Sales Ledger",
                "Audit sales, track settlement statuses, and export PDF invoices"));

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search invoice #, customer name...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setClearButtonVisible(true);
        searchField.addClassName("search-field");
        searchField.addValueChangeListener(e -> {
            search = e.getValue();
            refresh();
        });
        counts = invoices.counts();
        chips = new FilterChips<>(List.of(InvoiceFilter.values()), InvoiceFilter.ALL, this::chipLabel,
                InvoicesView::chipTone, selected -> refresh());
        Div toolbar = new Div(searchField, chips);
        toolbar.addClassNames("card", "toolbar", "toolbar-split");

        configureGrid();
        add(toolbar, tableHolder);
        refresh();
    }

    private String chipLabel(InvoiceFilter filter) {
        return filter.label() + " (" + counts.getOrDefault(filter, 0L) + ")";
    }

    private static String chipTone(InvoiceFilter filter) {
        return switch (filter) {
            case OVERDUE -> "red";
            case UNPAID -> "amber";
            case PAID -> "green";
            case PARTIAL -> "grey";
            case ALL -> null;
        };
    }

    private void configureGrid() {
        grid.addComponentColumn(i -> text(i.number(), "mono", "strong")).setHeader("Invoice #")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(InvoicesView::customerCell).setHeader("Customer").setWidth("240px").setFlexGrow(1);
        grid.addColumn(i -> Format.date(i.issuedOn())).setHeader("Date Issued").setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(InvoicesView::dueCell).setHeader("Due Date").setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(i -> text(Format.money(i.total()), "mono", "strong")).setHeader("Total")
                .setTextAlign(ColumnTextAlign.END).setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(i -> text(Format.money(i.paid()), "mono", "text-success")).setHeader("Paid")
                .setTextAlign(ColumnTextAlign.END).setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(InvoicesView::balanceCell).setHeader("Balance Due")
                .setTextAlign(ColumnTextAlign.END).setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(i -> InvoiceDetailDialog.statusBadge(i.status())).setHeader("Status")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(this::actions).setHeader("Actions").setTextAlign(ColumnTextAlign.END)
                .setAutoWidth(true).setFlexGrow(0);
        grid.setAllRowsVisible(true);
        grid.addThemeName("no-border");
        grid.addClassName("data-grid");
    }

    private void refresh() {
        counts = invoices.counts();
        chips.refreshLabels();
        List<InvoiceSummary> rows = invoices.list(search, chips.selected());
        tableHolder.removeAll();
        if (rows.isEmpty()) {
            Div empty = new Div(Panels.emptyState(VaadinIcon.CLIPBOARD_TEXT, "No invoices found",
                    "Make a sale at the register, or change the search and filter."));
            empty.addClassName("card");
            tableHolder.add(empty);
            return;
        }
        grid.setItems(rows);
        Div card = new Div(grid);
        card.addClassNames("card", "table-card");
        tableHolder.add(card);
    }

    private Div actions(InvoiceSummary invoice) {
        Div actions = new Div();
        actions.addClassName("row-actions");
        if (invoice.balance().signum() > 0) {
            Button pay = Panels.button("Pay", null, "btn-pay");
            pay.addClickListener(e -> PaymentDialog.open(invoice, invoices, () -> {
                refresh();
                MainLayout.refreshBadges();
            }));
            actions.add(pay);
        }
        Span downloadIcon = new Span(new Icon(VaadinIcon.DOWNLOAD));
        downloadIcon.addClassName("icon-link");
        Anchor download = pdf.invoice(invoice.id(), invoice.number(), downloadIcon);
        download.getElement().setAttribute("title", "Download PDF");
        Button view = Panels.iconButton(VaadinIcon.EYE, "View invoice");
        view.addClickListener(e -> InvoiceDetailDialog.open(invoices.detail(invoice.id()), pdf));
        actions.add(download, view);
        return actions;
    }

    private static Div customerCell(InvoiceSummary invoice) {
        Div cell = new Div(text(invoice.customerName(), "strong"));
        if (invoice.customerPhone() != null) {
            cell.add(text(invoice.customerPhone(), "mono", "muted", "small"));
        }
        cell.addClassName("stacked-cell");
        return cell;
    }

    private static Div dueCell(InvoiceSummary invoice) {
        boolean overdue = invoice.status() == InvoiceStatus.OVERDUE;
        Div cell = new Div(text(Format.date(invoice.dueOn()), overdue ? "text-danger" : "plain",
                overdue ? "strong" : "plain"));
        if (overdue) {
            cell.add(text("Overdue", "text-danger", "small"));
        }
        cell.addClassName("stacked-cell");
        return cell;
    }

    private static Span balanceCell(InvoiceSummary invoice) {
        return invoice.balance().signum() > 0
                ? text(Format.money(invoice.balance()), "mono", "strong", "text-danger")
                : text(Format.money(invoice.balance()), "mono", "muted");
    }

    private static Span text(String value, String... classes) {
        Span span = new Span(value);
        span.addClassNames(classes);
        return span;
    }
}
