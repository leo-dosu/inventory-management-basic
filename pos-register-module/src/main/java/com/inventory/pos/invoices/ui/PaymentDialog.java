package com.inventory.pos.invoices.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.invoices.service.InvoiceService;
import com.inventory.pos.invoices.service.InvoiceSummary;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Forms;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;

/** Records money received against one invoice. */
public final class PaymentDialog {

    private PaymentDialog() {
    }

    public static void open(InvoiceSummary invoice, InvoiceService invoices, Runnable onDone) {
        Dialog dialog = new Dialog();
        dialog.addClassName("pos-dialog");
        dialog.setWidth("460px");
        dialog.setHeaderTitle("Record payment");

        Div summary = new Div(
                Panels.detailRow("Invoice", invoice.number()),
                Panels.detailRow("Customer", invoice.customerName()),
                Panels.detailRow("Total", Format.money(invoice.total())),
                Panels.detailRow("Balance due", Format.money(invoice.balance())));
        summary.addClassName("detail-block");

        BigDecimalField amount = Forms.money("Amount received");
        amount.setValue(invoice.balance());
        Select<PaymentMethod> method = Forms.paymentMethod();
        TextField note = new TextField("Note (optional)");
        note.setWidthFull();
        Span error = Forms.errorLine();

        dialog.add(new Div(summary, amount, method, note, error));
        dialog.getFooter().add(cancel(dialog), save(dialog, invoice, invoices, amount, method, note, error, onDone));
        dialog.open();
        amount.focus();
    }

    private static Button cancel(Dialog dialog) {
        Button cancel = Panels.button("Cancel", null, "btn-secondary");
        cancel.addClickListener(e -> dialog.close());
        return cancel;
    }

    private static Button save(Dialog dialog, InvoiceSummary invoice, InvoiceService invoices,
                               BigDecimalField amount, Select<PaymentMethod> method, TextField note, Span error,
                               Runnable onDone) {
        Button save = Panels.button("Record payment", null, "btn-primary");
        save.addClickListener(e -> {
            try {
                invoices.recordPayment(invoice.id(), amount.getValue(), method.getValue(), note.getValue());
                dialog.close();
                Notify.success(Format.money(amount.getValue()) + " recorded against " + invoice.number() + ".");
                onDone.run();
            } catch (BusinessException ex) {
                Forms.show(error, ex.getMessage());
            }
        });
        return save;
    }
}
