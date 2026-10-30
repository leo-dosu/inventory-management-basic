package com.inventory.pos.debt.service;

import java.math.BigDecimal;
import java.util.List;

public record DebtPaymentResult(Long customerId, String customerName, BigDecimal amount,
                                List<PaymentAllocation> allocations, BigDecimal remainingDebt) {
}
