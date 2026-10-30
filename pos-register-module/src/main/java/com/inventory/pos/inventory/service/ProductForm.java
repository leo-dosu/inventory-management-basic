package com.inventory.pos.inventory.service;

import java.math.BigDecimal;

/** Add or edit a product. A null id means a new product. */
public record ProductForm(Long id, String sku, String name, String category, BigDecimal unitPrice, int stock,
                          String unit) {
}
