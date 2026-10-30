package com.inventory.pos.register.service;

import com.inventory.pos.core.domain.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

/** What the register sends when the cashier confirms a sale. A null customer is a walk-in. */
public record CheckoutRequest(Long customerId, List<CartLine> lines, BigDecimal discount, BigDecimal amountPaid,
                              PaymentMethod method) {
}
