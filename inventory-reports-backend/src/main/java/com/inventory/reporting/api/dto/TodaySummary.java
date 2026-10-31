package com.inventory.reporting.api.dto;

/**
 * The four cards on the Report statistics screen. Every change percentage compares today with yesterday.
 * sales = units sold in completed orders, totalOrders = all orders placed, revenue = value of completed orders,
 * customers = distinct customers who ordered.
 */
public record TodaySummary(KpiMetric sales, KpiMetric totalOrders, KpiMetric revenue, KpiMetric customers) {
}
