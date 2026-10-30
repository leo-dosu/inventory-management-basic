package com.inventory.pos.invoices.ui;

import com.inventory.pos.invoices.pdf.InvoicePdf;
import com.inventory.pos.invoices.service.InvoiceService;
import com.inventory.pos.shared.ui.Downloads;
import com.vaadin.flow.component.html.Anchor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** The link that downloads an invoice as a PDF. */
@Component
public class InvoiceDownloads {

    private final InvoiceService invoices;
    private final String businessName;

    public InvoiceDownloads(InvoiceService invoices, @Value("${pos.business-name}") String businessName) {
        this.invoices = invoices;
        this.businessName = businessName;
    }

    public Anchor invoice(Long invoiceId, String number, com.vaadin.flow.component.Component... content) {
        return Downloads.pdf(Downloads.safeName(number) + ".pdf",
                () -> InvoicePdf.render(businessName, invoices.detail(invoiceId)), content);
    }
}
