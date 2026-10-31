package com.inventory.reporting.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class PercentChange {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private PercentChange() {
    }

    /** Percentage change from previous to current, to one decimal place. No history counts as +100%, or 0% if still zero. */
    static BigDecimal between(BigDecimal current, BigDecimal previous) {
        if (previous.signum() == 0) {
            return current.signum() == 0
                    ? BigDecimal.ZERO.setScale(1)
                    : HUNDRED.setScale(1);
        }
        return current.subtract(previous)
                .multiply(HUNDRED)
                .divide(previous, 1, RoundingMode.HALF_UP);
    }
}
