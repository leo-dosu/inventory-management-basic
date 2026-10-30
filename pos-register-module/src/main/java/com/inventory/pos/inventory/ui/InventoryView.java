package com.inventory.pos.inventory.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.inventory.service.ProductService;
import com.inventory.pos.shared.ui.FilterChips;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.inventory.pos.shell.MainLayout;
import com.inventory.pos.shell.Routes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@StyleSheet("styles/inventory.css")
@Route(value = Routes.INVENTORY, layout = MainLayout.class)
@PageTitle("Inventory | Inventory")
public class InventoryView extends Div {

    private static final String ALL = "All";

    private final ProductService products;
    private final Grid<Product> grid = new Grid<>();
    private final Div tableHolder = new Div();
    private FilterChips<String> chips;
    private String search = "";
    private String category;

    public InventoryView(ProductService products) {
        this.products = products;
        addClassNames("page", "inventory-page");

        Button add = Panels.button("Add Product", VaadinIcon.PLUS, "btn-primary");
        add.addClickListener(e -> ProductDialog.open(products, null, this::reload));
        add(Panels.pageHeading("Inventory Catalog", "Manage product SKUs, pricing, and register stock levels", add));
        add(toolbar(), tableHolder);
        configureGrid();
        refresh();
    }

    private Div toolbar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search items by name or SKU...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setClearButtonVisible(true);
        searchField.addClassName("search-field");
        searchField.addValueChangeListener(e -> {
            search = e.getValue();
            refresh();
        });
        chips = buildChips();
        Div toolbar = new Div(searchField, chips);
        toolbar.addClassNames("card", "toolbar", "toolbar-split");
        return toolbar;
    }

    private FilterChips<String> buildChips() {
        List<String> options = new ArrayList<>(List.of(ALL));
        options.addAll(products.categories());
        return new FilterChips<>(options, category == null ? ALL : category, c -> c, null, selected -> {
            category = ALL.equals(selected) ? null : selected;
            refresh();
        });
    }

    private void configureGrid() {
        grid.addComponentColumn(p -> mono(p.getSku())).setHeader("SKU").setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(p -> strong(p.getName())).setHeader("Product Name").setFlexGrow(1);
        grid.addColumn(p -> p.getCategory() == null ? "" : p.getCategory()).setHeader("Category")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(p -> strong(Format.money(p.getUnitPrice()))).setHeader("Price")
                .setTextAlign(ColumnTextAlign.END).setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(InventoryView::stockCell).setHeader("Stock")
                .setTextAlign(ColumnTextAlign.END).setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(this::actions).setHeader("Actions").setTextAlign(ColumnTextAlign.END)
                .setAutoWidth(true).setFlexGrow(0);
        grid.setAllRowsVisible(true);
        grid.addThemeName("no-border");
        grid.addClassName("data-grid");
    }

    private void refresh() {
        List<Product> rows = products.list(search, category);
        tableHolder.removeAll();
        if (rows.isEmpty()) {
            Div empty = new Div(Panels.emptyState(VaadinIcon.CUBES, "No products found",
                    "Add a product, or try a different search or category."));
            empty.addClassName("card");
            tableHolder.add(empty);
            return;
        }
        grid.setItems(rows);
        Div card = new Div(grid);
        card.addClassNames("card", "table-card");
        tableHolder.add(card);
    }

    /** After an add, edit or delete the category list may have changed, so the chips are rebuilt too. */
    private void reload() {
        FilterChips<String> fresh = buildChips();
        chips.getParent().ifPresent(parent -> ((Div) parent).replace(chips, fresh));
        chips = fresh;
        refresh();
    }

    private Div actions(Product product) {
        Button edit = Panels.iconButton(VaadinIcon.EDIT, "Edit " + product.getName());
        edit.addClickListener(e -> ProductDialog.open(products, product, this::reload));
        Button delete = Panels.iconButton(VaadinIcon.TRASH, "Delete " + product.getName());
        delete.addClickListener(e -> confirmDelete(product));
        Div actions = new Div(edit, delete);
        actions.addClassName("row-actions");
        return actions;
    }

    private void confirmDelete(Product product) {
        ConfirmDialog confirm = new ConfirmDialog();
        confirm.setHeader("Delete " + product.getName() + "?");
        confirm.setText("It will disappear from the register. Invoices that already include it are not changed.");
        confirm.setCancelable(true);
        confirm.setConfirmText("Delete");
        confirm.addConfirmListener(e -> {
            try {
                products.delete(product.getId());
                Notify.success(product.getName() + " deleted.");
                reload();
            } catch (BusinessException ex) {
                Notify.error(ex.getMessage());
            }
        });
        confirm.open();
    }

    private static Span mono(String text) {
        Span span = new Span(text);
        span.addClassName("mono");
        return span;
    }

    private static Span strong(String text) {
        Span span = new Span(text);
        span.addClassName("strong");
        return span;
    }

    private static Span stockCell(Product product) {
        if (product.getStock() == 0) {
            Span out = new Span("Out of stock");
            out.addClassNames("mono", "text-danger");
            return out;
        }
        String unit = product.getUnit() == null ? "" : " " + product.getUnit();
        return mono(product.getStock() + unit);
    }
}
