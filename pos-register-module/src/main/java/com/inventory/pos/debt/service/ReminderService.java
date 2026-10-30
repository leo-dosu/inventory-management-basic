package com.inventory.pos.debt.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.Reminder;
import com.inventory.pos.core.repository.CustomerRepository;
import com.inventory.pos.core.repository.InvoiceRepository;
import com.inventory.pos.core.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Writes the reminder a debtor should receive and keeps a record that it was prepared. Nothing is sent from here. */
@Service
@Transactional(readOnly = true)
public class ReminderService {

    private final InvoiceRepository invoices;
    private final CustomerRepository customers;
    private final ReminderRepository reminders;
    private final Clock clock;
    private final String businessName;

    public ReminderService(InvoiceRepository invoices, CustomerRepository customers, ReminderRepository reminders,
                           Clock clock, @Value("${pos.business-name}") String businessName) {
        this.invoices = invoices;
        this.customers = customers;
        this.reminders = reminders;
        this.clock = clock;
        this.businessName = businessName;
    }

    /** Writes the reminder down and returns the wording, ready to send. */
    @Transactional
    public String prepareReminder(Long customerId) {
        Customer customer = customers.findById(customerId).orElseThrow(() -> new BusinessException("Customer not found."));
        List<Invoice> open = invoices.findByCustomer(customerId).stream().filter(i -> i.balance().signum() > 0).toList();
        if (open.isEmpty()) {
            throw new BusinessException(customer.getName() + " has nothing outstanding.");
        }
        BigDecimal owed = open.stream().map(Invoice::balance).reduce(BigDecimal.ZERO, BigDecimal::add);
        String numbers = open.stream().map(Invoice::getNumber).collect(Collectors.joining(", "));
        String message = "Dear " + customer.getName() + ", this is a reminder that ₦" + money(owed)
                + " is outstanding on " + (open.size() == 1 ? "invoice " : "invoices ") + numbers
                + ". The earliest was due on " + open.get(0).getDueOn()
                + ". Please arrange payment. Thank you, " + businessName + ".";
        reminders.save(new Reminder(customer, LocalDateTime.now(clock), message));
        return message;
    }

    private static String money(BigDecimal value) {
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }
}
