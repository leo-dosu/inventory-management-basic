package com.inventory.pos.customers.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.repository.CustomerRepository;
import com.inventory.pos.core.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private static final int DEFAULT_TERMS_DAYS = 14;

    private final CustomerRepository customers;
    private final InvoiceRepository invoices;

    public CustomerService(CustomerRepository customers, InvoiceRepository invoices) {
        this.customers = customers;
        this.invoices = invoices;
    }

    /** Every customer, alphabetically, filtered by name, phone or email. */
    public List<CustomerAccount> accounts(String search) {
        String query = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        Map<Long, List<Invoice>> byCustomer = invoices.findAllNewestFirst().stream()
                .filter(i -> i.getCustomer() != null)
                .collect(Collectors.groupingBy(i -> i.getCustomer().getId()));
        return customers.findAllByOrderByNameAsc().stream()
                .filter(c -> query.isEmpty() || matches(c, query))
                .map(c -> account(c, byCustomer.getOrDefault(c.getId(), List.of())))
                .toList();
    }

    public CustomerAccount find(Long id) {
        Customer customer = customers.findById(id).orElseThrow(() -> new BusinessException("Customer not found."));
        return account(customer, invoices.findByCustomer(id));
    }

    @Transactional
    public CustomerAccount create(CustomerForm form) {
        String name = form.name() == null ? "" : form.name().trim();
        if (name.isEmpty()) {
            throw new BusinessException("Enter the customer's name.");
        }
        int terms = form.termsDays() == null ? DEFAULT_TERMS_DAYS : form.termsDays();
        if (terms < 0 || terms > 365) {
            throw new BusinessException("Payment terms must be between 0 and 365 days.");
        }
        Customer saved = customers.save(new Customer(name, trim(form.phone()), trim(form.email()),
                trim(form.address()), trim(form.notes()), terms));
        return account(saved, List.of());
    }

    private static CustomerAccount account(Customer c, List<Invoice> theirInvoices) {
        BigDecimal spend = theirInvoices.stream().map(Invoice::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal debt = theirInvoices.stream().map(Invoice::balance).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CustomerAccount(c.getId(), c.getName(), c.getPhone(), c.getEmail(), c.getAddress(), c.getNotes(),
                c.getTermsDays(), theirInvoices.size(), spend, debt);
    }

    private static boolean matches(Customer c, String query) {
        return contains(c.getName(), query)
                || contains(c.getPhone(), query)
                || contains(c.getEmail(), query);
    }

    private static boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private static String trim(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
