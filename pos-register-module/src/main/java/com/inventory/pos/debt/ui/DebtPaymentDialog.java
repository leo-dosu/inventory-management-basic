package com.inventory.pos.debt.ui;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.debt.service.DebtPaymentResult;
import com.inventory.pos.debt.service.DebtPaymentService;
import com.inventory.pos.debt.service.Debtor;
import com.inventory.pos.debt.service.PaymentAllocation;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Forms;
import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;

import java.math.BigDecimal;
import java.util.List;

/**
 * Taking a payment from a debtor is two steps on purpose: enter the amount, then review exactly which invoices it
 * clears and confirm. Nothing is saved until the second button is pressed.
 */
public final class DebtPaymentDialog {

    private final Debtor debtor;
    private final DebtPaymentService debts;
    private final Runnable onDone;
    private final Dialog dialog = new Dialog();

    private DebtPaymentDialog(Debtor debtor, DebtPaymentService debts, Runnable onDone) {
        this.debtor = debtor;
        this.debts = debts;
        this.onDone = onDone;
        dialog.addClassName("pos-dialog");
        dialog.setWidth("500px");
        dialog.setHeaderTitle("Record payment: " + debtor.name());
    }

    public static void open(Debtor debtor, DebtPaymentService debts, Runnable onDone) {
        DebtPaymentDialog flow = new DebtPaymentDialog(debtor, debts, onDone);
        flow.enterAmount(debtor.balance(), PaymentMethod.CASH, "");
        flow.dialog.open();
    }

    private void enterAmount(BigDecimal amountValue, PaymentMethod methodValue, String noteValue) {
        dialog.removeAll();
        dialog.getFooter().removeAll();

        Div summary = new Div(
                Panels.detailRow("Customer", debtor.name()),
                Panels.detailRow("Unpaid invoices", String.valueOf(debtor.unpaidInvoices())),
                Panels.detailRow("Total owed", Format.money(debtor.balance())));
        summary.addClassName("detail-block");

        BigDecimalField amount = Forms.money("Amount received");
        amount.setValue(amountValue);
        Select<PaymentMethod> method = Forms.paymentMethod();
        method.setValue(methodValue);
        TextField note = new TextField("Note (optional)");
        note.setValue(noteValue);
        note.setWidthFull();
        Paragraph hint = new Paragraph("The payment clears the oldest invoice first.");
        hint.addClassName("form-hint");
        Span error = Forms.errorLine();
        dialog.add(new Div(summary, amount, method, note, hint, error));

        Button cancel = Panels.button("Cancel", null, "btn-secondary");
        cancel.addClickListener(e -> dialog.close());
        Button review = Panels.button("Review payment", VaadinIcon.CHECK, "btn-primary");
        review.addClickListener(e -> {
            try {
                List<PaymentAllocation> allocations = debts.preview(debtor.customerId(), amount.getValue());
                confirm(amount.getValue(), method.getValue(), note.getValue(), allocations);
            } catch (BusinessException ex) {
                Forms.show(error, ex.getMessage());
            }
        });
        dialog.getFooter().add(cancel, review);
        amount.focus();
    }

    private void confirm(BigDecimal amount, PaymentMethod method, String note, List<PaymentAllocation> allocations) {
        dialog.removeAll();
        dialog.getFooter().removeAll();

        Paragraph question = new Paragraph("Confirm that " + Format.money(amount) + " was received from "
                + debtor.name() + " by " + method.label().toLowerCase() + ".");
        question.addClassName("confirm-text");

        Div lines = new Div();
        lines.addClassName("allocation-table");
        lines.add(allocationRow("Invoice", "Applied", "Still owed", true));
        BigDecimal stillOwed = debtor.balance().subtract(amount);
        for (PaymentAllocation a : allocations) {
            lines.add(allocationRow(a.invoiceNumber(), Format.money(a.applied()),
                    Format.money(a.remainingBalance()), false));
        }
        Div after = new Div(Panels.detailRow("Debt after this payment", Format.money(stillOwed)));
        after.addClassNames("detail-block", "totals-block");
        Span error = Forms.errorLine();
        dialog.add(new Div(question, lines, after, error));

        Button back = Panels.button("Back", VaadinIcon.ANGLE_LEFT, "btn-secondary");
        back.addClickListener(e -> enterAmount(amount, method, note));
        Button confirm = Panels.button("Confirm payment", VaadinIcon.CHECK_CIRCLE, "btn-primary");
        confirm.addClickListener(e -> {
            try {
                DebtPaymentResult result = debts.recordPayment(debtor.customerId(), amount, method, note);
                dialog.close();
                Notify.success(Format.money(result.amount()) + " recorded for " + result.customerName() + ". "
                        + (result.remainingDebt().signum() == 0
                        ? "Their account is now settled."
                        : Format.money(result.remainingDebt()) + " still owed."));
                onDone.run();
            } catch (BusinessException ex) {
                Forms.show(error, ex.getMessage());
            }
        });
        dialog.getFooter().add(back, confirm);
    }

    private static Div allocationRow(String invoice, String applied, String remaining, boolean header) {
        Div row = new Div(new Span(invoice), new Span(applied), new Span(remaining));
        row.addClassNames("allocation-row", header ? "line-head" : "line-body");
        return row;
    }
}
