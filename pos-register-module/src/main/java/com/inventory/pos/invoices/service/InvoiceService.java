package com.inventory.pos.invoices.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.core.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** The invoice ledger: find invoices, look at one, and record money received against it. Sales are made by checkout. */
@Service
@Transactional(readOnly = true)
public class InvoiceService {

    private final InvoiceRepository invoices;
    private final Clock clock;

    public InvoiceService(InvoiceRepository invoices, Clock clock) {
        this.invoices = invoices;
        this.clock = clock;
    }

    /** Records money received against one invoice. */
    @Transactional
    public InvoiceDetail recordPayment(Long invoiceId, BigDecimal amount, PaymentMethod method, String note) {
        Invoice invoice = invoices.findById(invoiceId).orElseThrow(() -> new BusinessException("Invoice not found."));
        BigDecimal balance = invoice.balance();
        if (amount == null || amount.signum() <= 0) {
            throw new BusinessException("Enter an amount greater than zero.");
        }
        if (amount.compareTo(balance) > 0) {
            throw new BusinessException("That is more than the balance due on " + invoice.getNumber() + ".");
        }
        LocalDate today = LocalDate.now(clock);
        invoice.addPayment(amount.setScale(2, RoundingMode.HALF_UP), today,
                method == null ? PaymentMethod.CASH : method, blankToNull(note));
        return InvoiceDetail.of(invoice, today);
    }

    /** Invoices matching the search (invoice number or customer name) and status filter, newest first. */
    public List<InvoiceSummary> list(String search, InvoiceFilter filter) {
        String query = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        LocalDate today = LocalDate.now(clock);
        return invoices.findAllNewestFirst().stream()
                .map(i -> InvoiceSummary.of(i, today))
                .filter(s -> filter.matches(s.status()))
                .filter(s -> query.isEmpty()
                        || s.number().toLowerCase(Locale.ROOT).contains(query)
                        || s.customerName().toLowerCase(Locale.ROOT).contains(query))
                .toList();
    }

    /** How many invoices fall under each filter chip, ignoring the search text. */
    public Map<InvoiceFilter, Long> counts() {
        LocalDate today = LocalDate.now(clock);
        List<InvoiceSummary> all = invoices.findAllNewestFirst().stream()
                .map(i -> InvoiceSummary.of(i, today)).toList();
        Map<InvoiceFilter, Long> counts = new EnumMap<>(InvoiceFilter.class);
        for (InvoiceFilter filter : InvoiceFilter.values()) {
            counts.put(filter, all.stream().filter(s -> filter.matches(s.status())).count());
        }
        return counts;
    }

    public InvoiceDetail detail(Long id) {
        Invoice invoice = invoices.findById(id).orElseThrow(() -> new BusinessException("Invoice not found."));
        return InvoiceDetail.of(invoice, LocalDate.now(clock));
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
