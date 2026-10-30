package com.inventory.pos.debt.pdf;

import com.inventory.pos.debt.service.Statement;
import com.inventory.pos.invoices.service.InvoiceSummary;
import com.inventory.pos.shared.pdf.Pdf;
import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

public final class StatementPdf {

    private StatementPdf() {
    }

    public static byte[] render(String businessName, Statement statement) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 48, 48);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable head = Pdf.table(3, 3);
            head.addCell(Pdf.plain(businessName, Pdf.TITLE, Pdf.left()));
            head.addCell(Pdf.plain("STATEMENT", Pdf.TITLE, Pdf.right()));
            head.addCell(Pdf.plain("Statement of account", Pdf.SMALL, Pdf.left()));
            head.addCell(Pdf.plain("As at " + statement.asOf(), Pdf.BODY_BOLD, Pdf.right()));
            document.add(head);
            document.add(new Paragraph(" "));

            document.add(new Paragraph(statement.customerName(), Pdf.HEADING));
            for (String detail : new String[]{statement.phone(), statement.email(), statement.address()}) {
                if (detail != null) {
                    document.add(new Paragraph(detail, Pdf.BODY));
                }
            }
            document.add(new Paragraph("Payment terms: " + statement.termsDays() + " days", Pdf.SMALL));
            document.add(new Paragraph(" "));

            PdfPTable table = Pdf.table(3, 2, 2, 2, 2, 2, 2);
            table.addCell(Pdf.headerCell("Invoice", Pdf.left()));
            table.addCell(Pdf.headerCell("Issued", Pdf.left()));
            table.addCell(Pdf.headerCell("Due", Pdf.left()));
            table.addCell(Pdf.headerCell("Total", Pdf.right()));
            table.addCell(Pdf.headerCell("Paid", Pdf.right()));
            table.addCell(Pdf.headerCell("Balance", Pdf.right()));
            table.addCell(Pdf.headerCell("Status", Pdf.left()));
            for (InvoiceSummary invoice : statement.invoices()) {
                table.addCell(Pdf.cell(invoice.number(), Pdf.BODY_BOLD, Pdf.left()));
                table.addCell(Pdf.cell(invoice.issuedOn().toString(), Pdf.BODY, Pdf.left()));
                table.addCell(Pdf.cell(invoice.dueOn().toString(), Pdf.BODY, Pdf.left()));
                table.addCell(Pdf.cell(Pdf.money(invoice.total()), Pdf.BODY, Pdf.right()));
                table.addCell(Pdf.cell(Pdf.money(invoice.paid()), Pdf.BODY, Pdf.right()));
                table.addCell(Pdf.cell(Pdf.money(invoice.balance()), Pdf.BODY, Pdf.right()));
                table.addCell(Pdf.cell(invoice.status().name(), Pdf.SMALL, Pdf.left()));
            }
            document.add(table);
            document.add(new Paragraph(" "));

            PdfPTable totals = Pdf.table(6, 3);
            totals.addCell(Pdf.plain("", Pdf.BODY, Pdf.left()));
            totals.addCell(Pdf.plain("Total billed", Pdf.BODY, Pdf.right()));
            totals.addCell(Pdf.plain(Pdf.money(statement.totalBilled()), Pdf.BODY, Pdf.right()));
            totals.addCell(Pdf.plain("", Pdf.BODY, Pdf.left()));
            totals.addCell(Pdf.plain("Total paid", Pdf.BODY, Pdf.right()));
            totals.addCell(Pdf.plain(Pdf.money(statement.totalPaid()), Pdf.BODY, Pdf.right()));
            totals.addCell(Pdf.plain("", Pdf.BODY, Pdf.left()));
            totals.addCell(Pdf.plain("Total outstanding", Pdf.BODY_BOLD, Pdf.right()));
            totals.addCell(Pdf.plain(Pdf.money(statement.totalOutstanding()), Pdf.BODY_BOLD, Pdf.right()));
            document.add(totals);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Please settle any outstanding balance by the due date. Thank you.", Pdf.SMALL));
            document.close();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create the statement for " + statement.customerName(), e);
        }
        return out.toByteArray();
    }
}
