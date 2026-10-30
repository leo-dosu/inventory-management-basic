package com.inventory.pos.support;

import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.core.repository.CustomerRepository;
import com.inventory.pos.core.repository.InvoiceRepository;
import com.inventory.pos.core.repository.ProductRepository;
import com.inventory.pos.customers.service.CustomerService;
import com.inventory.pos.debt.service.DebtPaymentService;
import com.inventory.pos.debt.service.DebtService;
import com.inventory.pos.debt.service.ReminderService;
import com.inventory.pos.debt.service.StatementService;
import com.inventory.pos.inventory.service.ProductService;
import com.inventory.pos.invoices.service.InvoiceService;
import com.inventory.pos.register.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Start every service test from this class. It gives you H2, all the services, a clock fixed at 28 October 2026,
 * a 10% tax rate (so sums are easy to check by hand) and three rows to work with: two products and one customer.
 */
@DataJpaTest
@Import({CheckoutService.class, InvoiceService.class, DebtService.class, DebtPaymentService.class,
        ReminderService.class, StatementService.class, CustomerService.class, ProductService.class,
        PosDataTest.FixedClock.class})
@TestPropertySource(properties = {"pos.tax-rate=0.10", "pos.business-name=Test Shop"})
public abstract class PosDataTest {

    protected static final LocalDate TODAY = LocalDate.of(2026, 10, 28);

    @TestConfiguration
    public static class FixedClock {
        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2026-10-28T10:00:00Z"), ZoneOffset.UTC);
        }
    }

    @Autowired protected ProductRepository products;
    @Autowired protected CustomerRepository customers;
    @Autowired protected InvoiceRepository invoices;

    /** 10 in stock at 100. */
    protected Product mouse;
    /** 5 in stock at 50. */
    protected Product cable;
    /** A customer on 14-day terms. */
    protected Customer ada;

    @BeforeEach
    void seedBaseData() {
        mouse = products.save(new Product("MOU-1", "Mouse", "Accessories", new BigDecimal("100.00"), 10, "pc"));
        cable = products.save(new Product("CAB-1", "Cable", "Accessories", new BigDecimal("50.00"), 5, "pc"));
        ada = customers.save(new Customer("Ada Stores", "0800", "ada@example.com", "Lagos", null, 14));
    }

    /** An invoice for one line of {@code amount}, with no tax, that fell due {@code daysLate} days ago. */
    protected Invoice overdue(String number, Customer customer, int daysLate, String amount) {
        Invoice invoice = new Invoice(number, customer, TODAY.minusDays(daysLate + 14L), TODAY.minusDays(daysLate),
                BigDecimal.ZERO);
        invoice.addLine(null, null, "Goods", 1, new BigDecimal(amount));
        invoice.applyDiscount(BigDecimal.ZERO);
        return invoice;
    }
}
