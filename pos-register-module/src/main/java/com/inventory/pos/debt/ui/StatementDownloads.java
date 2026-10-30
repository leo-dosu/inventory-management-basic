package com.inventory.pos.debt.ui;

import com.inventory.pos.debt.pdf.StatementPdf;
import com.inventory.pos.debt.service.StatementService;
import com.inventory.pos.shared.ui.Downloads;
import com.vaadin.flow.component.html.Anchor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** The link that downloads a customer's statement of account as a PDF. */
@Component
public class StatementDownloads {

    private final StatementService statements;
    private final String businessName;

    public StatementDownloads(StatementService statements, @Value("${pos.business-name}") String businessName) {
        this.statements = statements;
        this.businessName = businessName;
    }

    public Anchor statement(Long customerId, String customerName, com.vaadin.flow.component.Component... content) {
        return Downloads.pdf("Statement-" + Downloads.safeName(customerName) + ".pdf",
                () -> StatementPdf.render(businessName, statements.statement(customerId)), content);
    }
}
