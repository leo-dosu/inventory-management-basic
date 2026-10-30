package com.inventory.pos.debt.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.core.repository.CustomerRepository;
import com.inventory.pos.core.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Taking money from a debtor and sharing it across their unpaid invoices. */
@Service
@Transactional(readOnly = true)
public class DebtPaymentService {

    private final InvoiceRepository invoices;
    private final CustomerRepository customers;
    private final Clock clock;

    public DebtPaymentService(InvoiceRepository invoices, CustomerRepository customers, Clock clock) {
        this.invoices = invoices;
        this.customers = customers;
        this.clock = clock;
    }

    /** Shows how a payment would be shared across the customer's invoices, without saving anything. */
    public List<PaymentAllocation> preview(Long customerId, BigDecimal amount) {
        return allocate(openInvoicesOf(customerId), validAmount(customerId, amount), null, null, null);
    }

    /**
     * Takes a payment from a customer and spreads it over their unpaid invoices, oldest due date first, so the most
     * overdue debt is cleared before anything newer.
     */
    @Transactional
    public DebtPaymentResult recordPayment(Long customerId, BigDecimal amount, PaymentMethod method, String note) {
        Customer customer = customers.findById(customerId).orElseThrow(() -> new BusinessException("Customer not found."));
        List<Invoice> open = openInvoicesOf(customerId);
        BigDecimal paid = validAmount(customerId, amount);
        List<PaymentAllocation> allocations = allocate(open, paid, method == null ? PaymentMethod.CASH : method,
                note == null || note.isBlank() ? "Debt payment" : note.trim(), LocalDate.now(clock));
        BigDecimal remaining = open.stream().map(Invoice::balance).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DebtPaymentResult(customer.getId(), customer.getName(), paid, allocations, remaining);
    }

    private List<Invoice> openInvoicesOf(Long customerId) {
        return invoices.findByCustomer(customerId).stream().filter(i -> i.balance().signum() > 0).toList();
    }

    private BigDecimal validAmount(Long customerId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new BusinessException("Enter an amount greater than zero.");
        }
        BigDecimal owed = openInvoicesOf(customerId).stream().map(Invoice::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (owed.signum() == 0) {
            throw new BusinessException("This customer has nothing outstanding.");
        }
        BigDecimal rounded = amount.setScale(2, RoundingMode.HALF_UP);
        if (rounded.compareTo(owed) > 0) {
            throw new BusinessException("That is more than the ₦" + money(owed) + " this customer owes.");
        }
        return rounded;
    }

    /** Spreads {@code amount} over {@code open}. When {@code method} is given the payments are actually recorded. */
    private static List<PaymentAllocation> allocate(List<Invoice> open, BigDecimal amount, PaymentMethod method,
                                                    String note, LocalDate today) {
        List<PaymentAllocation> result = new ArrayList<>();
        BigDecimal left = amount;
        for (Invoice invoice : open) {
            if (left.signum() == 0) {
                break;
            }
            BigDecimal applied = left.min(invoice.balance());
            BigDecimal remaining = invoice.balance().subtract(applied);
            if (method != null) {
                invoice.addPayment(applied, today, method, note);
            }
            result.add(new PaymentAllocation(invoice.getNumber(), applied, remaining));
            left = left.subtract(applied);
        }
        return result;
    }

    private static String money(BigDecimal value) {
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }
}
