package com.inventory.pos.debt.pdf;

import com.inventory.pos.core.domain.InvoiceStatus;
import com.inventory.pos.debt.service.Statement;
import com.inventory.pos.invoices.service.InvoiceSummary;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StatementPdfTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 28);

    @Test
    void statementIsARealPdf() {
        InvoiceSummary invoice = new InvoiceSummary(1L, "INV-2026-001", 7L, "Ada Stores", "0800", TODAY.minusDays(20),
                TODAY.minusDays(6), new BigDecimal("220.00"), new BigDecimal("20.00"), new BigDecimal("200.00"),
                InvoiceStatus.OVERDUE, 6);
        Statement statement = new Statement(7L, "Ada Stores", "0800", "ada@example.com", "Lagos", 14,
                List.of(invoice), new BigDecimal("220.00"), new BigDecimal("20.00"), new BigDecimal("200.00"), TODAY);

        byte[] pdf = StatementPdf.render("Test Shop", statement);

        assertThat(new String(pdf, 0, 5, StandardCharsets.US_ASCII)).isEqualTo("%PDF-");
        assertThat(pdf.length).isGreaterThan(1000);
    }
}
