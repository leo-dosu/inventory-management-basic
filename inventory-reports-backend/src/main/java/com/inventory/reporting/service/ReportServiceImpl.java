package com.inventory.reporting.service;

import com.inventory.reporting.api.ReportService;
import com.inventory.reporting.api.dto.DailyOrdersVsSales;
import com.inventory.reporting.api.dto.KpiMetric;
import com.inventory.reporting.api.dto.TodaySummary;
import com.inventory.reporting.api.dto.TopSellingProduct;
import com.inventory.reporting.domain.OrderStatus;
import com.inventory.reporting.domain.SalesOrder;
import com.inventory.reporting.repository.OrderItemRepository;
import com.inventory.reporting.repository.SalesOrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private static final int MAX_DAYS = 366;

    private final SalesOrderRepository orders;
    private final OrderItemRepository items;
    private final Clock clock;

    public ReportServiceImpl(SalesOrderRepository orders, OrderItemRepository items, Clock clock) {
        this.orders = orders;
        this.items = items;
        this.clock = clock;
    }

    @Override
    public TodaySummary getTodaySummary() {
        LocalDate today = LocalDate.now(clock);
        LocalDate yesterday = today.minusDays(1);
        List<SalesOrder> recent = orders.findWithItemsBetween(yesterday, today);

        DayFigures now = DayFigures.of(recent, today);
        DayFigures before = DayFigures.of(recent, yesterday);

        return new TodaySummary(
                metric(BigDecimal.valueOf(now.units()), BigDecimal.valueOf(before.units())),
                metric(BigDecimal.valueOf(now.orders()), BigDecimal.valueOf(before.orders())),
                metric(now.revenue(), before.revenue()),
                metric(BigDecimal.valueOf(now.customers()), BigDecimal.valueOf(before.customers())));
    }

    @Override
    public List<DailyOrdersVsSales> getOrdersVsSales(int days) {
        if (days < 1 || days > MAX_DAYS) {
            throw new IllegalArgumentException("days must be between 1 and " + MAX_DAYS);
        }
        LocalDate today = LocalDate.now(clock);
        LocalDate start = today.minusDays(days - 1L);
        List<SalesOrder> inRange = orders.findWithItemsBetween(start, today);

        List<DailyOrdersVsSales> series = new ArrayList<>(days);
        for (LocalDate day = start; !day.isAfter(today); day = day.plusDays(1)) {
            DayFigures figures = DayFigures.of(inRange, day);
            series.add(new DailyOrdersVsSales(day, figures.orders(), figures.revenue()));
        }
        return series;
    }

    @Override
    public List<TopSellingProduct> getTopSellingProducts(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be at least 1");
        }
        return items.findTopSelling(OrderStatus.COMPLETED, PageRequest.of(0, limit));
    }

    private static KpiMetric metric(BigDecimal current, BigDecimal previous) {
        return new KpiMetric(current, PercentChange.between(current, previous));
    }

    private record DayFigures(long units, long orders, BigDecimal revenue, long customers) {

        static DayFigures of(List<SalesOrder> all, LocalDate day) {
            List<SalesOrder> placed = all.stream().filter(o -> o.getOrderDate().equals(day)).toList();
            List<SalesOrder> completed = placed.stream()
                    .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                    .toList();
            long units = completed.stream().mapToLong(SalesOrder::unitCount).sum();
            BigDecimal revenue = completed.stream().map(SalesOrder::total).reduce(BigDecimal.ZERO, BigDecimal::add);
            long customers = placed.stream().map(o -> o.getCustomer().getId()).collect(Collectors.toSet()).size();
            return new DayFigures(units, placed.size(), revenue, customers);
        }
    }
}
