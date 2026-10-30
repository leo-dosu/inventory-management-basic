package com.inventory.pos.register.ui;

import com.inventory.pos.customers.service.CustomerService;
import com.inventory.pos.inventory.service.ProductService;
import com.inventory.pos.invoices.ui.InvoiceDownloads;
import com.inventory.pos.register.service.CheckoutService;
import com.inventory.pos.shell.MainLayout;
import com.inventory.pos.shell.Routes;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/** The register screen. It only joins the two halves: the catalogue on the left and the cart on the right. */
@StyleSheet("styles/register.css")
@Route(value = Routes.REGISTER, layout = MainLayout.class)
@PageTitle("POS Register | Inventory")
public class PosView extends Div {

    private final ProductCatalogue catalogue;
    private final CartPanel cartPanel;

    public PosView(ProductService products, CustomerService customers, CheckoutService checkout,
                   InvoiceDownloads pdf) {
        addClassName("pos-page");
        cartPanel = new CartPanel(customers, checkout, pdf, this::saleCompleted);
        catalogue = new ProductCatalogue(products, cartPanel::addProduct,
                () -> CustomItemDialog.open(cartPanel::addCustom));
        add(catalogue, cartPanel);
    }

    private void saleCompleted() {
        catalogue.refresh();
    }
}
