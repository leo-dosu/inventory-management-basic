package com.inventory.pos.register.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.customers.service.CustomerAccount;
import com.inventory.pos.invoices.service.InvoiceDetail;
import com.inventory.pos.register.service.CartLine;
import com.inventory.pos.register.service.CheckoutRequest;
import com.inventory.pos.register.service.CheckoutService;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Forms;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

/**
 * The payment step of a sale. A walk-in pays the whole total. A customer account may pay part now: the rest becomes
 * a debt that falls due after their payment terms.
 */
public final class CheckoutDialog {

    private CheckoutDialog() {
    }

    public static void open(CheckoutService checkout, CustomerAccount customer, List<CartLine> lines,
                            BigDecimal discount, BigDecimal total, Consumer<InvoiceDetail> onSold) {
        Dialog dialog = new Dialog();
        dialog.addClassName("pos-dialog");
        dialog.setWidth("480px");
        dialog.setHeaderTitle("Complete sale");

        Span due = new Span(Format.money(total));
        due.addClassName("big-total");
        Div summary = new Div(
                Panels.detailRow("Customer", customer == null ? "Walk-in Retail Guest" : customer.name()),
                Panels.detailRow("Total due", due));
        summary.addClassName("detail-block");

        Select<PaymentMethod> method = Forms.paymentMethod();
        BigDecimalField received = Forms.money("Amount received");
        received.setValue(total);
        received.setValueChangeMode(ValueChangeMode.EAGER);

        Paragraph note = new Paragraph();
        note.addClassName("form-hint");
        Runnable updateNote = () -> {
            BigDecimal paid = received.getValue() == null ? BigDecimal.ZERO : received.getValue();
            BigDecimal left = total.subtract(paid);
            if (customer == null) {
                note.setText("Walk-in sales are paid in full.");
            } else if (left.signum() > 0) {
                note.setText(Format.money(left) + " will go on " + customer.name() + "'s account, due "
                        + Format.date(LocalDate.now().plusDays(customer.termsDays())) + ".");
            } else {
                note.setText("Paid in full.");
            }
        };
        received.addValueChangeListener(e -> updateNote.run());
        updateNote.run();
        Span error = Forms.errorLine();
        dialog.add(new Div(summary, method, received, note, error));

        Button cancel = Panels.button("Cancel", null, "btn-secondary");
        cancel.addClickListener(e -> dialog.close());
        Button confirm = Panels.button("Confirm sale", VaadinIcon.CHECK_CIRCLE, "btn-primary");
        confirm.addClickListener(e -> {
            try {
                InvoiceDetail sold = checkout.checkout(new CheckoutRequest(customer == null ? null : customer.id(),
                        lines, discount, received.getValue(), method.getValue()));
                dialog.close();
                onSold.accept(sold);
            } catch (BusinessException ex) {
                Forms.show(error, ex.getMessage());
            }
        });
        dialog.getFooter().add(cancel, confirm);
        dialog.open();
        received.focus();
    }
}
