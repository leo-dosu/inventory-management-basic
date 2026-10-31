package com.inventory.reporting.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PercentChangeTest {

    @Test
    void increaseIsPositive() {
        assertThat(PercentChange.between(new BigDecimal("150"), new BigDecimal("100"))).isEqualByComparingTo("50.0");
    }

    @Test
    void decreaseIsNegative() {
        assertThat(PercentChange.between(new BigDecimal("75"), new BigDecimal("100"))).isEqualByComparingTo("-25.0");
    }

    @Test
    void roundsToOneDecimal() {
        assertThat(PercentChange.between(new BigDecimal("2"), new BigDecimal("3"))).isEqualByComparingTo("-33.3");
    }

    @Test
    void noHistoryCountsAsFullGrowth() {
        assertThat(PercentChange.between(new BigDecimal("5"), BigDecimal.ZERO)).isEqualByComparingTo("100.0");
    }

    @Test
    void nothingThenNothingIsZero() {
        assertThat(PercentChange.between(BigDecimal.ZERO, BigDecimal.ZERO)).isEqualByComparingTo("0.0");
    }
}
