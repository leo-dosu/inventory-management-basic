package com.inventory.reporting.service;

import com.inventory.reporting.api.ReportService;
import com.inventory.reporting.api.dto.TodaySummary;
import com.inventory.reporting.api.dto.TopSellingProduct;
import com.inventory.reporting.domain.Customer;
import com.inventory.reporting.domain.OrderStatus;
import com.inventory.reporting.domain.Product;
import com.inventory.reporting.domain.SalesOrder;
import com.inventory.reporting.repository.CustomerRepository;
import com.inventory.reporting.repository.ProductRepository;
import com.inventory.reporting.repository.SalesOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Runs the real JPQL queries against H2 through the real service. */
@DataJpaTest
@Import({ReportServiceImpl.class, ReportingIntegrationTest.FixedClock.class})
class ReportingIntegrationTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 28);

    @TestConfiguration
    static class FixedClock {
        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2026-10-28T10:00:00Z"), ZoneOffset.UTC);
        }
    }

    @Autowired
    private ReportService reports;
    @Autowired
    private ProductRepository products;
    @Autowired
    private CustomerRepository customers;
    @Autowired
    private SalesOrderRepository orders;

    @BeforeEach
    void seed() {
        Product mouse = products.save(new Product("P-1", "Mouse", "Accessories", new BigDecimal("100.00")));
        Product cable = products.save(new Product("P-3", "Cable", "Electronics", new BigDecimal("50.00")));
        Customer customer = customers.save(new Customer("Ada"));

        SalesOrder completed = new SalesOrder("O-1", TODAY, customer, "Store", "Lagos", OrderStatus.COMPLETED);
        completed.addItem(mouse, 3);
        completed.addItem(cable, 1);
        SalesOrder returned = new SalesOrder("O-2", TODAY, customer, "Store", "Lagos", OrderStatus.RETURNED);
        returned.addItem(cable, 2);
        SalesOrder pending = new SalesOrder("O-3", TODAY, customer, "Store", "Lagos", OrderStatus.PENDING);
        pending.addItem(mouse, 10);
        orders.saveAll(List.of(completed, returned, pending));
    }

    @Test
    void topSellingCountsCompletedOrdersOnly() {
        List<TopSellingProduct> top = reports.getTopSellingProducts(5);

        assertThat(top).extracting(TopSellingProduct::sku).containsExactly("P-1", "P-3");
        assertThat(top.get(0).unitsSold()).isEqualTo(3L);
        assertThat(top.get(0).revenue()).isEqualByComparingTo("300");
        assertThat(top.get(1).unitsSold()).isEqualTo(1L);
    }

    @Test
    void topSellingHonoursLimit() {
        assertThat(reports.getTopSellingProducts(1)).hasSize(1);
    }

    @Test
    void todaySummaryUsesStoredOrders() {
        TodaySummary summary = reports.getTodaySummary();

        assertThat(summary.totalOrders().value()).isEqualByComparingTo("3");
        assertThat(summary.sales().value()).isEqualByComparingTo("4");
        assertThat(summary.revenue().value()).isEqualByComparingTo("350");
        assertThat(summary.customers().value()).isEqualByComparingTo("1");
        assertThat(summary.revenue().changePercent()).isEqualByComparingTo("100.0");
    }
}
