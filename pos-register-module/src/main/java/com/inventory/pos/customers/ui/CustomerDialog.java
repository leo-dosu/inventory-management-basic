package com.inventory.pos.customers.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.customers.service.CustomerAccount;
import com.inventory.pos.customers.service.CustomerForm;
import com.inventory.pos.customers.service.CustomerService;
import com.inventory.pos.shared.ui.Forms;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;

/** Adds a customer account. Used from both the Customers screen and the register. */
public final class CustomerDialog {

    private CustomerDialog() {
    }

    public static void open(CustomerService customers, Consumer<CustomerAccount> onCreated) {
        Dialog dialog = new Dialog();
        dialog.addClassName("pos-dialog");
        dialog.setWidth("520px");
        dialog.setHeaderTitle("Add new customer");

        TextField name = new TextField("Name");
        name.setRequiredIndicatorVisible(true);
        TextField phone = new TextField("Phone");
        EmailField email = new EmailField("Email");
        TextField address = new TextField("Address");
        TextArea notes = new TextArea("Notes");
        IntegerField terms = new IntegerField("Payment terms (days)");
        terms.setValue(14);
        terms.setMin(0);
        terms.setMax(365);
        terms.setStepButtonsVisible(true);
        for (HasSize field : new HasSize[]{name, phone, email, address, notes, terms}) {
            field.setWidthFull();
        }
        Span error = Forms.errorLine();

        dialog.add(new Div(name, phone, email, address, notes, terms, error));

        Button cancel = Panels.button("Cancel", null, "btn-secondary");
        cancel.addClickListener(e -> dialog.close());
        Button save = Panels.button("Add customer", null, "btn-primary");
        save.addClickListener(e -> {
            try {
                CustomerAccount created = customers.create(new CustomerForm(name.getValue(), phone.getValue(),
                        email.getValue(), address.getValue(), notes.getValue(), terms.getValue()));
                dialog.close();
                Notify.success(created.name() + " added.");
                onCreated.accept(created);
            } catch (BusinessException ex) {
                Forms.show(error, ex.getMessage());
            }
        });
        dialog.getFooter().add(cancel, save);
        dialog.open();
        name.focus();
    }
}
