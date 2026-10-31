package com.inventory.reporting.service;

import com.inventory.reporting.api.dto.DailyOrdersVsSales;
import com.inventory.reporting.api.dto.TodaySummary;
import com.inventory.reporting.domain.Customer;
import com.inventory.reporting.domain.OrderStatus;
import com.inventory.reporting.domain.Product;
import com.inventory.reporting.domain.SalesOrder;
import com.inventory.reporting.repository.OrderItemRepository;
import com.inventory.reporting.repository.SalesOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 28);
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);

    @Mock
    private SalesOrderRepository orders;
    @Mock
    private OrderItemRepository items;

    private ReportServiceImpl service;
    private Product widget;
    private Customer ada;
    private Customer bola;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-10-28T10:00:00Z"), ZoneOffset.UTC);
        service = new ReportServiceImpl(orders, items, clock);
        widget = new Product("W-1", "Widget", "Test", new BigDecimal("100.00"));
        ada = customer(1L, "Ada");
        bola = customer(2L, "Bola");
    }

    @Test
    void todaySummaryComparesWithYesterday() {
        when(orders.findWithItemsBetween(YESTERDAY, TODAY)).thenReturn(List.of(
                order("A", TODAY, ada, OrderStatus.COMPLETED, 3),
                order("B", TODAY, bola, OrderStatus.PENDING, 1),
                order("C", YESTERDAY, ada, OrderStatus.COMPLETED, 2)));

        TodaySummary summary = service.getTodaySummary();

        assertThat(summary.sales().value()).isEqualByComparingTo("3");
        assertThat(summary.sales().changePercent()).isEqualByComparingTo("50.0");
        assertThat(summary.totalOrders().value()).isEqualByComparingTo("2");
        assertThat(summary.totalOrders().changePercent()).isEqualByComparingTo("100.0");
        assertThat(summary.revenue().value()).isEqualByComparingTo("300");
        assertThat(summary.revenue().changePercent()).isEqualByComparingTo("50.0");
        assertThat(summary.customers().value()).isEqualByComparingTo("2");
        assertThat(summary.customers().changePercent()).isEqualByComparingTo("100.0");
    }

    @Test
    void todaySummaryWithNoOrdersIsAllZero() {
        when(orders.findWithItemsBetween(YESTERDAY, TODAY)).thenReturn(List.of());

        TodaySummary summary = service.getTodaySummary();

        assertThat(summary.revenue().value()).isEqualByComparingTo("0");
        assertThat(summary.revenue().changePercent()).isEqualByComparingTo("0");
        assertThat(summary.totalOrders().value()).isEqualByComparingTo("0");
    }

    @Test
    void ordersVsSalesFillsQuietDaysWithZero() {
        when(orders.findWithItemsBetween(TODAY.minusDays(2), TODAY)).thenReturn(List.of(
                order("A", TODAY, ada, OrderStatus.COMPLETED, 1),
                order("B", TODAY, bola, OrderStatus.PENDING, 1),
                order("C", YESTERDAY, ada, OrderStatus.COMPLETED, 2)));

        List<DailyOrdersVsSales> series = service.getOrdersVsSales(3);

        assertThat(series).extracting(DailyOrdersVsSales::date)
                .containsExactly(TODAY.minusDays(2), YESTERDAY, TODAY);
        assertThat(series).extracting(DailyOrdersVsSales::orders).containsExactly(0L, 1L, 2L);
        assertThat(series.get(0).sales()).isEqualByComparingTo("0");
        assertThat(series.get(1).sales()).isEqualByComparingTo("200");
        assertThat(series.get(2).sales()).isEqualByComparingTo("100");
    }

    @Test
    void rejectsBadArguments() {
        assertThatThrownBy(() -> service.getOrdersVsSales(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.getOrdersVsSales(1000)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.getTopSellingProducts(0)).isInstanceOf(IllegalArgumentException.class);
    }

    private SalesOrder order(String number, LocalDate date, Customer customer, OrderStatus status, int quantity) {
        SalesOrder order = new SalesOrder(number, date, customer, "Store", "Lagos", status);
        order.addItem(widget, quantity);
        return order;
    }

    private static Customer customer(long id, String name) {
        Customer customer = new Customer(name);
        ReflectionTestUtils.setField(customer, "id", id);
        return customer;
    }
      }
