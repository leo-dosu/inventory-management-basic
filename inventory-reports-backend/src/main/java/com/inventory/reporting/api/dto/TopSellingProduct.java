package com.inventory.reporting.api.dto;

import java.math.BigDecimal;

/** One slice of the Top selling products chart and one row of its table. */
public record TopSellingProduct(Long productId, String sku, String name, Long unitsSold, BigDecimal revenue) {
}
