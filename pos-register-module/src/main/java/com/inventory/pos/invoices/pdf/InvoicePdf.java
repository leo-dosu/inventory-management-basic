package com.inventory.pos.invoices.pdf;

import com.inventory.pos.shared.pdf.Pdf;
import com.inventory.pos.invoices.service.InvoiceDetail;
import com.inventory.pos.invoices.service.InvoiceSummary;
import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class InvoicePdf {

    private InvoicePdf() {
    }

    public static byte[] render(String businessName, InvoiceDetail detail) {
        InvoiceSummary s = detail.summary();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 48, 48);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable head = Pdf.table(3, 2);
            head.addCell(Pdf.plain(businessName, Pdf.TITLE, Pdf.left()));
            head.addCell(Pdf.plain("INVOICE", Pdf.TITLE, Pdf.right()));
            head.addCell(Pdf.plain("Sales invoice", Pdf.SMALL, Pdf.left()));
            head.addCell(Pdf.plain(s.number(), Pdf.BODY_BOLD, Pdf.right()));
            document.add(head);
            document.add(new Paragraph(" "));

            PdfPTable meta = Pdf.table(1, 1);
            meta.addCell(Pdf.plain("BILL TO", Pdf.SMALL, Pdf.left()));
            meta.addCell(Pdf.plain("DETAILS", Pdf.SMALL, Pdf.right()));
            meta.addCell(Pdf.plain(s.customerName(), Pdf.BODY_BOLD, Pdf.left()));
            meta.addCell(Pdf.plain("Issued: " + s.issuedOn(), Pdf.BODY, Pdf.right()));
            meta.addCell(Pdf.plain(s.customerPhone() == null ? "" : s.customerPhone(), Pdf.BODY, Pdf.left()));
            meta.addCell(Pdf.plain("Due: " + s.dueOn(), Pdf.BODY, Pdf.right()));
            meta.addCell(Pdf.plain("", Pdf.BODY, Pdf.left()));
            meta.addCell(Pdf.plain("Status: " + s.status(), Pdf.BODY_BOLD, Pdf.right()));
            document.add(meta);
            document.add(new Paragraph(" "));

            PdfPTable lines = Pdf.table(5, 2, 1, 2, 2);
            lines.addCell(Pdf.headerCell("Description", Pdf.left()));
            lines.addCell(Pdf.headerCell("SKU", Pdf.left()));
            lines.addCell(Pdf.headerCell("Qty", Pdf.right()));
            lines.addCell(Pdf.headerCell("Unit price", Pdf.right()));
            lines.addCell(Pdf.headerCell("Total", Pdf.right()));
            for (InvoiceDetail.Line line : detail.lines()) {
                lines.addCell(Pdf.cell(line.description(), Pdf.BODY, Pdf.left()));
                lines.addCell(Pdf.cell(line.sku() == null ? "Custom" : line.sku(), Pdf.SMALL, Pdf.left()));
                lines.addCell(Pdf.cell(String.valueOf(line.quantity()), Pdf.BODY, Pdf.right()));
                lines.addCell(Pdf.cell(Pdf.money(line.unitPrice()), Pdf.BODY, Pdf.right()));
                lines.addCell(Pdf.cell(Pdf.money(line.lineTotal()), Pdf.BODY, Pdf.right()));
            }
            document.add(lines);
            document.add(new Paragraph(" "));

            PdfPTable totals = Pdf.table(6, 3);
            total(totals, "Subtotal", Pdf.money(detail.subtotal()), false);
            total(totals, "Discount", "- " + Pdf.money(detail.discount()), false);
            String rate = detail.taxRate().multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP)
                    .stripTrailingZeros().toPlainString();
            total(totals, "Tax (" + rate + "%)", Pdf.money(detail.tax()), false);
            total(totals, "Total", Pdf.money(s.total()), true);
            total(totals, "Paid", Pdf.money(s.paid()), false);
            total(totals, "Balance due", Pdf.money(s.balance()), true);
            document.add(totals);

            if (!detail.payments().isEmpty()) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Payments received", Pdf.HEADING));
                PdfPTable payments = Pdf.table(2, 2, 2, 3);
                payments.addCell(Pdf.headerCell("Date", Pdf.left()));
                payments.addCell(Pdf.headerCell("Method", Pdf.left()));
                payments.addCell(Pdf.headerCell("Amount", Pdf.right()));
                payments.addCell(Pdf.headerCell("Note", Pdf.left()));
                for (InvoiceDetail.PaymentEntry p : detail.payments()) {
                    payments.addCell(Pdf.cell(p.paidOn().toString(), Pdf.BODY, Pdf.left()));
                    payments.addCell(Pdf.cell(p.method().label(), Pdf.BODY, Pdf.left()));
                    payments.addCell(Pdf.cell(Pdf.money(p.amount()), Pdf.BODY, Pdf.right()));
                    payments.addCell(Pdf.cell(p.note(), Pdf.SMALL, Pdf.left()));
                }
                document.add(payments);
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for your business.", Pdf.SMALL));
            document.close();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create the PDF for " + s.number(), e);
        }
        return out.toByteArray();
    }

    private static void total(PdfPTable table, String label, String value, boolean bold) {
        table.addCell(Pdf.plain("", Pdf.BODY, Pdf.left()));
        table.addCell(Pdf.plain(label, bold ? Pdf.BODY_BOLD : Pdf.BODY, Pdf.right()));
        table.addCell(Pdf.plain(value, bold ? Pdf.BODY_BOLD : Pdf.BODY, Pdf.right()));
    }
}
