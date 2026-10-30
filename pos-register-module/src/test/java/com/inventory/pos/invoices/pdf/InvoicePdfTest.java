package com.inventory.pos.invoices.pdf;

import com.inventory.pos.core.domain.InvoiceStatus;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.invoices.service.InvoiceDetail;
import com.inventory.pos.invoices.service.InvoiceSummary;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvoicePdfTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 28);

    @Test
    void invoiceIsARealPdf() {
        InvoiceSummary summary = new InvoiceSummary(1L, "INV-2026-001", 7L, "Ada Stores", "0800", TODAY.minusDays(20),
                TODAY.minusDays(6), new BigDecimal("220.00"), new BigDecimal("20.00"), new BigDecimal("200.00"),
                InvoiceStatus.OVERDUE, 6);
        InvoiceDetail detail = new InvoiceDetail(summary, new BigDecimal("200.00"), BigDecimal.ZERO,
                new BigDecimal("0.10"), new BigDecimal("20.00"),
                List.of(new InvoiceDetail.Line("MOU-1", "Mouse", 2, new BigDecimal("100.00"), new BigDecimal("200.00")),
                        new InvoiceDetail.Line(null, "Delivery & setup <fee>", 1, BigDecimal.TEN, BigDecimal.TEN)),
                List.of(new InvoiceDetail.PaymentEntry(TODAY, new BigDecimal("20.00"), PaymentMethod.CASH, "Deposit")));

        byte[] pdf = InvoicePdf.render("Test Shop", detail);

        assertThat(new String(pdf, 0, 5, StandardCharsets.US_ASCII)).isEqualTo("%PDF-");
        assertThat(pdf.length).isGreaterThan(1000);
    }
}
