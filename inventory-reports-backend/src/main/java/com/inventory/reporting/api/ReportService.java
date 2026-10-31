package com.inventory.reporting.api;

import com.inventory.reporting.api.dto.DailyOrdersVsSales;
import com.inventory.reporting.api.dto.TodaySummary;
import com.inventory.reporting.api.dto.TopSellingProduct;

import java.util.List;

/** Data behind the Report statistics screen. */
public interface ReportService {

    TodaySummary getTodaySummary();

    /** One entry per day for the last {@code days} days ending today, oldest first, with zeros for quiet days. */
    List<DailyOrdersVsSales> getOrdersVsSales(int days);

    List<TopSellingProduct> getTopSellingProducts(int limit);
}
