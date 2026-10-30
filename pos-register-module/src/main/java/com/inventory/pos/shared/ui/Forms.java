package com.inventory.pos.shared.ui;

import com.inventory.pos.core.domain.PaymentMethod;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;

/** Form fields and the inline error line that every dialog shares. */
public final class Forms {

    private Forms() {
    }

    public static BigDecimalField money(String label) {
        BigDecimalField field = new BigDecimalField(label);
        field.setPrefixComponent(new Span("₦"));
        field.setWidthFull();
        return field;
    }

    public static Select<PaymentMethod> paymentMethod() {
        Select<PaymentMethod> method = new Select<>();
        method.setLabel("Payment method");
        method.setItems(PaymentMethod.values());
        method.setItemLabelGenerator(PaymentMethod::label);
        method.setValue(PaymentMethod.CASH);
        method.setWidthFull();
        return method;
    }

    public static Span errorLine() {
        Span error = new Span();
        error.addClassName("form-error");
        error.setVisible(false);
        return error;
    }

    public static void show(Span error, String message) {
        error.setText(message);
        error.setVisible(true);
    }
}
