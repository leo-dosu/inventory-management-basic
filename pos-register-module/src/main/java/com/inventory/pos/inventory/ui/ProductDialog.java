package com.inventory.pos.inventory.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.inventory.service.ProductForm;
import com.inventory.pos.inventory.service.ProductService;
import com.inventory.pos.shared.ui.Forms;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.math.BigDecimal;

/** Adds a product or edits an existing one (pass it as {@code existing}). */
public final class ProductDialog {

    private ProductDialog() {
    }

    public static void open(ProductService products, Product existing, Runnable onSaved) {
        boolean editing = existing != null;
        Dialog dialog = new Dialog();
        dialog.addClassName("pos-dialog");
        dialog.setWidth("520px");
        dialog.setHeaderTitle(editing ? "Edit product" : "Add product");

        TextField sku = new TextField("SKU");
        TextField name = new TextField("Product name");
        ComboBox<String> category = new ComboBox<>("Category");
        category.setItems(products.categories());
        category.setAllowCustomValue(true);
        category.addCustomValueSetListener(e -> category.setValue(e.getDetail()));
        BigDecimalField price = Forms.money("Price");
        IntegerField stock = new IntegerField("Stock on hand");
        stock.setMin(0);
        stock.setStepButtonsVisible(true);
        stock.setValue(0);
        TextField unit = new TextField("Unit (bag, carton, loaf)");
        for (HasSize field : new HasSize[]{sku, name, category, stock, unit}) {
            field.setWidthFull();
        }
        Span error = Forms.errorLine();

        if (editing) {
            sku.setValue(existing.getSku());
            name.setValue(existing.getName());
            category.setValue(existing.getCategory());
            price.setValue(existing.getUnitPrice());
            stock.setValue(existing.getStock());
            unit.setValue(existing.getUnit() == null ? "" : existing.getUnit());
        } else {
            price.setValue(BigDecimal.ZERO);
        }
        dialog.add(new Div(sku, name, category, price, stock, unit, error));

        Button cancel = Panels.button("Cancel", null, "btn-secondary");
        cancel.addClickListener(e -> dialog.close());
        Button save = Panels.button(editing ? "Save changes" : "Add product", null, "btn-primary");
        save.addClickListener(e -> {
            try {
                products.save(new ProductForm(editing ? existing.getId() : null, sku.getValue(), name.getValue(),
                        category.getValue(), price.getValue(), stock.getValue() == null ? 0 : stock.getValue(),
                        unit.getValue()));
                dialog.close();
                Notify.success(name.getValue() + (editing ? " updated." : " added."));
                onSaved.run();
            } catch (BusinessException ex) {
                Forms.show(error, ex.getMessage());
            }
        });
        dialog.getFooter().add(cancel, save);
        dialog.open();
        sku.focus();
    }
}
