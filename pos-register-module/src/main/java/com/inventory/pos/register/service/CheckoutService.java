package com.inventory.pos.register.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.core.repository.CustomerRepository;
import com.inventory.pos.core.repository.InvoiceRepository;
import com.inventory.pos.core.repository.ProductRepository;
import com.inventory.pos.invoices.service.InvoiceDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Locale;

/** Turns a cart into an invoice: the rules of a sale at the register. */
@Service
@Transactional(readOnly = true)
public class CheckoutService {

    private final InvoiceRepository invoices;
    private final ProductRepository products;
    private final CustomerRepository customers;
    private final Clock clock;
    private final BigDecimal taxRate;

    public CheckoutService(InvoiceRepository invoices, ProductRepository products, CustomerRepository customers,
                           Clock clock, @Value("${pos.tax-rate}") BigDecimal taxRate) {
        this.invoices = invoices;
        this.products = products;
        this.customers = customers;
        this.clock = clock;
        this.taxRate = taxRate;
    }

    public BigDecimal taxRate() {
        return taxRate;
    }

    /**
     * Turns the cart into an invoice, takes the stock off the shelf and records what was paid. A walk-in must pay
     * in full; a customer account may pay part now and owe the rest until the invoice is due.
     */
    @Transactional
    public InvoiceDetail checkout(CheckoutRequest request) {
        if (request.lines() == null || request.lines().isEmpty()) {
            throw new BusinessException("The register is empty.");
        }
        LocalDate today = LocalDate.now(clock);
        Customer customer = request.customerId() == null ? null : customers.findById(request.customerId())
                .orElseThrow(() -> new BusinessException("Customer not found."));
        LocalDate dueOn = customer == null ? today : today.plusDays(customer.getTermsDays());

        Invoice invoice = new Invoice(nextNumber(today), customer, today, dueOn, taxRate);
        for (CartLine line : request.lines()) {
            addLine(invoice, line);
        }

        BigDecimal discount = zeroIfNull(request.discount()).setScale(2, RoundingMode.HALF_UP);
        if (discount.signum() < 0 || discount.compareTo(invoice.getSubtotal()) > 0) {
            throw new BusinessException("The discount must be between zero and the subtotal.");
        }
        invoice.applyDiscount(discount);

        BigDecimal paid = zeroIfNull(request.amountPaid()).setScale(2, RoundingMode.HALF_UP);
        if (paid.signum() < 0 || paid.compareTo(invoice.getTotal()) > 0) {
            throw new BusinessException("The amount received cannot be more than the total due.");
        }
        if (customer == null && paid.compareTo(invoice.getTotal()) != 0) {
            throw new BusinessException("A walk-in sale must be paid in full. Choose a customer account to sell on credit.");
        }
        if (paid.signum() > 0) {
            PaymentMethod method = request.method() == null ? PaymentMethod.CASH : request.method();
            invoice.addPayment(paid, today, method, "Paid at the register");
        }
        return InvoiceDetail.of(invoices.save(invoice), today);
    }

    private void addLine(Invoice invoice, CartLine line) {
        if (line.quantity() < 1) {
            throw new BusinessException("Every quantity must be at least 1.");
        }
        if (line.productId() != null) {
            Product product = products.findById(line.productId())
                    .orElseThrow(() -> new BusinessException("A product in the cart no longer exists."));
            if (product.getStock() < line.quantity()) {
                throw new BusinessException("Only " + product.getStock() + " of " + product.getName() + " in stock.");
            }
            product.setStock(product.getStock() - line.quantity());
            invoice.addLine(product.getId(), product.getSku(), product.getName(), line.quantity(),
                    product.getUnitPrice());
            return;
        }
        String description = line.description() == null ? "" : line.description().trim();
        if (description.isEmpty()) {
            throw new BusinessException("Give the custom item a description.");
        }
        if (line.unitPrice() == null || line.unitPrice().signum() < 0) {
            throw new BusinessException("Enter a price of zero or more for " + description + ".");
        }
        invoice.addLine(null, null, description, line.quantity(), line.unitPrice().setScale(2, RoundingMode.HALF_UP));
    }

    private String nextNumber(LocalDate today) {
        LocalDate start = today.withDayOfYear(1);
        LocalDate end = today.withDayOfYear(today.lengthOfYear());
        long sequence = invoices.countByIssuedOnBetween(start, end) + 1;
        return String.format(Locale.ROOT, "INV-%d-%03d", today.getYear(), sequence);
    }

    private static BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
