package com.inventory.pos.register.ui;

import com.inventory.pos.core.domain.Product;
import com.inventory.pos.register.service.CartLine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** What the cashier has rung up so far. It lives in the browser session until the sale is confirmed. */
final class Cart {

    static final class Entry {
        final Long productId;
        final String sku;
        final String name;
        final BigDecimal unitPrice;
        final int max;
        int quantity;

        Entry(Long productId, String sku, String name, BigDecimal unitPrice, int quantity, int max) {
            this.productId = productId;
            this.sku = sku;
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.max = max;
        }

        BigDecimal lineTotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }

        boolean isCustom() {
            return productId == null;
        }
    }

    private final List<Entry> entries = new ArrayList<>();

    List<Entry> entries() {
        return entries;
    }

    boolean isEmpty() {
        return entries.isEmpty();
    }

    void clear() {
        entries.clear();
    }

    /** Adds one of the product. Returns false when the cart already holds all the stock there is. */
    boolean add(Product product) {
        for (Entry entry : entries) {
            if (product.getId().equals(entry.productId)) {
                return increment(entry);
            }
        }
        if (product.getStock() < 1) {
            return false;
        }
        entries.add(new Entry(product.getId(), product.getSku(), product.getName(), product.getUnitPrice(), 1,
                product.getStock()));
        return true;
    }

    void addCustom(String description, BigDecimal price, int quantity) {
        entries.add(new Entry(null, null, description, price, quantity, Integer.MAX_VALUE));
    }

    boolean increment(Entry entry) {
        if (entry.quantity >= entry.max) {
            return false;
        }
        entry.quantity++;
        return true;
    }

    void decrement(Entry entry) {
        if (entry.quantity <= 1) {
            entries.remove(entry);
        } else {
            entry.quantity--;
        }
    }

    void remove(Entry entry) {
        entries.remove(entry);
    }

    BigDecimal subtotal() {
        return entries.stream().map(Entry::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    List<CartLine> lines() {
        return entries.stream()
                .map(e -> new CartLine(e.productId, e.isCustom() ? e.name : null, e.quantity,
                        e.isCustom() ? e.unitPrice : null))
                .toList();
    }
}
