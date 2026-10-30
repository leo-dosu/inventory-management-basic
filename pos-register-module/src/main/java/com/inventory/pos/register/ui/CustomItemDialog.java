package com.inventory.pos.register.ui;

import com.inventory.pos.shared.ui.Forms;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.math.BigDecimal;

/** A one-off line for something that is not in the catalogue, such as a delivery fee. It touches no stock. */
public final class CustomItemDialog {

    /** Called with the description, unit price and quantity once the form is valid. */
    public interface OnAdd {
        void add(String description, BigDecimal price, int quantity);
    }

    private CustomItemDialog() {
    }

    public static void open(OnAdd onAdd) {
        Dialog dialog = new Dialog();
        dialog.addClassName("pos-dialog");
        dialog.setWidth("440px");
        dialog.setHeaderTitle("Add custom item");

        TextField description = new TextField("Description");
        description.setWidthFull();
        BigDecimalField price = Forms.money("Unit price");
        IntegerField quantity = new IntegerField("Quantity");
        quantity.setValue(1);
        quantity.setMin(1);
        quantity.setStepButtonsVisible(true);
        quantity.setWidthFull();
        Span error = Forms.errorLine();
        dialog.add(new Div(description, price, quantity, error));

        Button cancel = Panels.button("Cancel", null, "btn-secondary");
        cancel.addClickListener(e -> dialog.close());
        Button add = Panels.button("Add to cart", null, "btn-primary");
        add.addClickListener(e -> {
            if (description.getValue().isBlank()) {
                Forms.show(error, "Describe the item.");
            } else if (price.getValue() == null || price.getValue().signum() < 0) {
                Forms.show(error, "Enter a price of zero or more.");
            } else if (quantity.getValue() == null || quantity.getValue() < 1) {
                Forms.show(error, "The quantity must be at least 1.");
            } else {
                dialog.close();
                onAdd.add(description.getValue().trim(), price.getValue(), quantity.getValue());
            }
        });
        dialog.getFooter().add(cancel, add);
        dialog.open();
        description.focus();
    }
}
