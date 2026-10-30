package com.inventory.pos.register.ui;

import com.inventory.pos.core.domain.Product;
import com.inventory.pos.inventory.service.ProductService;
import com.inventory.pos.shared.ui.FilterChips;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** The left half of the register: search, category chips and the grid of products to click into the cart. */
public class ProductCatalogue extends Div {

    private static final String ALL = "All";

    private final ProductService products;
    private final Consumer<Product> onPick;
    private final Div productGrid = new Div();
    private String search = "";
    private String category;

    /**
     * @param onPick       called when a product card is clicked
     * @param onCustomItem called when the Custom Item button is pressed
     */
    public ProductCatalogue(ProductService products, Consumer<Product> onPick, Runnable onCustomItem) {
        this.products = products;
        this.onPick = onPick;
        addClassName("pos-left");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search products by name or SKU...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setClearButtonVisible(true);
        searchField.addClassName("search-field");
        searchField.addValueChangeListener(e -> {
            search = e.getValue();
            refresh();
        });

        Button custom = Panels.button("Custom Item", VaadinIcon.PLUS, "btn-secondary");
        custom.addClickListener(e -> onCustomItem.run());

        List<String> options = new ArrayList<>(List.of(ALL));
        options.addAll(products.categories());
        FilterChips<String> chips = new FilterChips<>(options, ALL, c -> c, null, selected -> {
            category = ALL.equals(selected) ? null : selected;
            refresh();
        });

        Div top = new Div(searchField, custom);
        top.addClassName("toolbar-row");
        Div toolbar = new Div(top, chips);
        toolbar.addClassNames("card", "toolbar");

        productGrid.addClassName("product-grid");
        add(toolbar, productGrid);
        refresh();
    }

    /** Re-reads the products, for example after a sale has taken stock off the shelf. */
    public void refresh() {
        productGrid.removeAll();
        List<Product> shown = products.list(search, category);
        if (shown.isEmpty()) {
            productGrid.add(Panels.emptyState(VaadinIcon.CUBES, "No products found",
                    "Try a different search or category."));
            return;
        }
        shown.forEach(p -> productGrid.add(productCard(p)));
    }

    private Div productCard(Product product) {
        boolean out = product.getStock() < 1;
        Span sku = new Span(product.getSku());
        sku.addClassName("product-sku");
        Span stock = new Span(out ? "Out of stock" : product.getStock() + " in stock");
        stock.addClassNames("product-stock", out ? "out" : "in");
        Div top = new Div(sku, stock);
        top.addClassName("product-top");

        Div name = new Div(new Span(product.getName()));
        name.addClassName("product-name");

        Span price = new Span(Format.money(product.getUnitPrice()));
        price.addClassName("product-price");
        Span add = new Span(out ? "Unavailable" : "+ Add");
        add.addClassName("product-add");
        Div bottom = new Div(price, add);
        bottom.addClassName("product-bottom");

        Div card = new Div(top, name, bottom);
        card.addClassName("product-card");
        card.getClassNames().set("unavailable", out);
        card.addClickListener(e -> onPick.accept(product));
        return card;
    }
}
