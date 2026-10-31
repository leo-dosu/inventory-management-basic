package com.inventory.reporting.web;

import com.inventory.reporting.api.ReportService;
import com.inventory.reporting.api.dto.DailyOrdersVsSales;
import com.inventory.reporting.api.dto.TodaySummary;
import com.inventory.reporting.api.dto.TopSellingProduct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reports;

    public ReportController(ReportService reports) {
        this.reports = reports;
    }

    @GetMapping("/today")
    public TodaySummary today() {
        return reports.getTodaySummary();
    }

    @GetMapping("/orders-vs-sales")
    public List<DailyOrdersVsSales> ordersVsSales(@RequestParam(defaultValue = "7") int days) {
        return reports.getOrdersVsSales(days);
    }

    @GetMapping("/top-selling")
    public List<TopSellingProduct> topSelling(@RequestParam(defaultValue = "5") int limit) {
        return reports.getTopSellingProducts(limit);
    }
}
