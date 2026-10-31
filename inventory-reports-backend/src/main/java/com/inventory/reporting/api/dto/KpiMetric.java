package com.inventory.reporting.api.dto;

import java.math.BigDecimal;

/** A headline number with its percentage change against the previous period. */
public record KpiMetric(BigDecimal value, BigDecimal changePercent) {
}
