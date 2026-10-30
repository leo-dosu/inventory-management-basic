package com.inventory.pos.debt.service;

import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.repository.InvoiceRepository;
import com.inventory.pos.core.repository.PaymentRepository;
import com.inventory.pos.invoices.service.InvoiceSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Who owes money and how late it is: the summary cards and the debtor list. Taking payments is
 * {@link DebtPaymentService}, chasing is {@link ReminderService}, statements are {@link StatementService}.
 */
@Service
@Transactional(readOnly = true)
public class DebtService {

    private static final int DUE_SOON_DAYS = 7;

    private final InvoiceRepository invoices;
    private final PaymentRepository payments;
    private final Clock clock;

    public DebtService(InvoiceRepository invoices, PaymentRepository payments, Clock clock) {
        this.invoices = invoices;
        this.payments = payments;
        this.clock = clock;
    }

    public DebtOverview overview() {
        LocalDate today = LocalDate.now(clock);
        List<Invoice> open = invoices.findOpenWithCustomer();
        long debtors = open.stream().map(i -> i.getCustomer().getId()).distinct().count();
        return new DebtOverview(invoices.sumOutstanding(), open.size(), invoices.sumOverdue(today),
                payments.sumAll(), debtors);
    }

    /** Customers who owe money, biggest balance first, filtered by name, phone, email or invoice number. */
    public List<Debtor> debtors(String search, DebtFilter filter) {
        String query = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        LocalDate today = LocalDate.now(clock);
        Map<Long, List<Invoice>> byCustomer = invoices.findOpenWithCustomer().stream()
                .collect(Collectors.groupingBy(i -> i.getCustomer().getId(), LinkedHashMap::new, Collectors.toList()));
        return byCustomer.values().stream()
                .filter(list -> query.isEmpty() || matches(list, query))
                .map(list -> debtor(list, today))
                .filter(filter::matches)
                .sorted(Comparator.comparing(Debtor::balance).reversed())
                .toList();
    }

    /** A customer's unpaid invoices, oldest due date first. */
    public List<InvoiceSummary> openInvoices(Long customerId) {
        LocalDate today = LocalDate.now(clock);
        return invoices.findByCustomer(customerId).stream()
                .filter(i -> i.balance().signum() > 0)
                .map(i -> InvoiceSummary.of(i, today))
                .toList();
    }

    private static Debtor debtor(List<Invoice> list, LocalDate today) {
        Customer customer = list.get(0).getCustomer();
        BigDecimal balance = list.stream().map(Invoice::balance).reduce(BigDecimal.ZERO, BigDecimal::add);
        long maxOverdue = list.stream().filter(i -> i.getDueOn().isBefore(today))
                .mapToLong(i -> ChronoUnit.DAYS.between(i.getDueOn(), today)).max().orElse(0);
        boolean dueSoon = list.stream().anyMatch(i -> !i.getDueOn().isBefore(today)
                && !i.getDueOn().isAfter(today.plusDays(DUE_SOON_DAYS)));
        LocalDate earliest = list.stream().map(Invoice::getDueOn).min(Comparator.naturalOrder()).orElse(null);
        return new Debtor(customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail(),
                list.size(), balance, maxOverdue > 0, maxOverdue, dueSoon, earliest);
    }

    private static boolean matches(List<Invoice> list, String query) {
        Customer c = list.get(0).getCustomer();
        return contains(c.getName(), query) || contains(c.getPhone(), query) || contains(c.getEmail(), query)
                || list.stream().anyMatch(i -> contains(i.getNumber(), query));
    }

    private static boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }
}
