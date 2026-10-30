package com.inventory.pos.register.service;

import java.math.BigDecimal;

/**
 * One line the cashier has rung up. For a catalogue product only {@code productId} and {@code quantity} are trusted:
 * the name and price come from the product. A custom item has no product id and carries its own description and price.
 */
public record CartLine(Long productId, String description, int quantity, BigDecimal unitPrice) {
}
