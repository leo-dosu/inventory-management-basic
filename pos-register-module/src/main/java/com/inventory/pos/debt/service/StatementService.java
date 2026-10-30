package com.inventory.pos.debt.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.repository.CustomerRepository;
import com.inventory.pos.core.repository.InvoiceRepository;
import com.inventory.pos.invoices.service.InvoiceSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/** Builds the statement of account for one customer. */
@Service
@Transactional(readOnly = true)
public class StatementService {

    private final CustomerRepository customers;
    private final InvoiceRepository invoices;
    private final Clock clock;

    public StatementService(CustomerRepository customers, InvoiceRepository invoices, Clock clock) {
        this.customers = customers;
        this.invoices = invoices;
        this.clock = clock;
    }

    public Statement statement(Long customerId) {
        Customer customer = customers.findById(customerId)
                .orElseThrow(() -> new BusinessException("Customer not found."));
        LocalDate today = LocalDate.now(clock);
        List<InvoiceSummary> rows = invoices.findByCustomer(customerId).stream()
                .map(i -> InvoiceSummary.of(i, today))
                .toList();
        BigDecimal billed = rows.stream().map(InvoiceSummary::total).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paid = rows.stream().map(InvoiceSummary::paid).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Statement(customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail(),
                customer.getAddress(), customer.getTermsDays(), rows, billed, paid, billed.subtract(paid), today);
    }
}
