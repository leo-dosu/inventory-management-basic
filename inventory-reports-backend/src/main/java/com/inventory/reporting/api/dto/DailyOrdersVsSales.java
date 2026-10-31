package com.inventory.reporting.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One point of the Orders vs Sales bar chart: orders placed that day and the value of completed orders. */
public record DailyOrdersVsSales(LocalDate date, long orders, BigDecimal sales) {
}
