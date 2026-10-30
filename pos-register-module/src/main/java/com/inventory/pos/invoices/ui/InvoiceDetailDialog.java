package com.inventory.pos.invoices.ui;

import com.inventory.pos.core.domain.InvoiceStatus;
import com.inventory.pos.invoices.service.InvoiceDetail;
import com.inventory.pos.invoices.service.InvoiceSummary;
import com.inventory.pos.shared.ui.Format;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** The full invoice on screen: who, when, what was sold, the sums, and every payment so far. */
public final class InvoiceDetailDialog {

    private InvoiceDetailDialog() {
    }

    public static Span statusBadge(InvoiceStatus status) {
        return switch (status) {
            case PAID -> Panels.badge("PAID", "success");
            case PARTIAL -> Panels.badge("PARTIAL", "warning");
            case UNPAID -> Panels.badge("UNPAID", "info");
            case OVERDUE -> Panels.badge("OVERDUE", "danger");
        };
    }

    public static void open(InvoiceDetail detail, InvoiceDownloads pdf) {
        InvoiceSummary s = detail.summary();
        Dialog dialog = new Dialog();
        dialog.addClassName("pos-dialog");
        dialog.setWidth("560px");
        dialog.setHeaderTitle("Invoice " + s.number());

        Div meta = new Div(
                Panels.detailRow("Customer", s.customerName()),
                Panels.detailRow("Issued", Format.date(s.issuedOn())),
                Panels.detailRow("Due", Format.date(s.dueOn())),
                Panels.detailRow("Status", statusBadge(s.status())));
        meta.addClassName("detail-block");

        Div lines = new Div();
        lines.addClassName("line-table");
        lines.add(lineRow("Item", "Qty", "Price", "Total", true));
        for (InvoiceDetail.Line line : detail.lines()) {
            String name = line.sku() == null ? line.description() : line.description() + "  ·  " + line.sku();
            lines.add(lineRow(name, String.valueOf(line.quantity()), Format.money(line.unitPrice()),
                    Format.money(line.lineTotal()), false));
        }

        String rate = detail.taxRate().multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString();
        Div totals = new Div(
                Panels.detailRow("Subtotal", Format.money(detail.subtotal())),
                Panels.detailRow("Discount", "-" + Format.money(detail.discount())),
                Panels.detailRow("Tax (" + rate + "%)", Format.money(detail.tax())),
                Panels.detailRow("Total", Format.money(s.total())),
                Panels.detailRow("Paid", Format.money(s.paid())),
                Panels.detailRow("Balance due", Format.money(s.balance())));
        totals.addClassNames("detail-block", "totals-block");

        Div content = new Div(meta, lines, totals);
        if (!detail.payments().isEmpty()) {
            Div payments = new Div(Panels.sectionTitle("Payments received"));
            payments.addClassName("detail-block");
            for (InvoiceDetail.PaymentEntry p : detail.payments()) {
                String note = p.note() == null ? "" : "  ·  " + p.note();
                payments.add(Panels.detailRow(Format.date(p.paidOn()) + "  ·  " + p.method().label() + note,
                        Format.money(p.amount())));
            }
            content.add(payments);
        }
        dialog.add(content);

        Button close = Panels.button("Close", null, "btn-secondary");
        close.addClickListener(e -> dialog.close());
        Button download = Panels.button("Download PDF", VaadinIcon.DOWNLOAD, "btn-primary");
        dialog.getFooter().add(close, pdf.invoice(s.id(), s.number(), download));
        dialog.open();
    }

    private static Div lineRow(String name, String qty, String price, String total, boolean header) {
        Div row = new Div(new Span(name), new Span(qty), new Span(price), new Span(total));
        row.addClassNames("line-row", header ? "line-head" : "line-body");
        return row;
    }
}
