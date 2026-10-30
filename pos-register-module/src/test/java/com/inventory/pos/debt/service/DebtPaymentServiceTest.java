package com.inventory.pos.debt.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.support.PosDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DebtPaymentServiceTest extends PosDataTest {

    @Autowired
    private DebtPaymentService paymentService;
    @Autowired
    private DebtService debtService;

    @Test
    void aDebtPaymentClearsTheOldestInvoiceFirst() {
        invoices.save(overdue("INV-OLD", ada, 10, "100.00"));
        invoices.save(overdue("INV-NEW", ada, 3, "200.00"));

        List<PaymentAllocation> preview = paymentService.preview(ada.getId(), new BigDecimal("150.00"));
        assertThat(preview).extracting(PaymentAllocation::invoiceNumber).containsExactly("INV-OLD", "INV-NEW");
        assertThat(debtService.overview().outstanding()).isEqualByComparingTo("300");

        DebtPaymentResult result = paymentService.recordPayment(ada.getId(), new BigDecimal("150.00"),
                PaymentMethod.TRANSFER, "Part settlement");

        assertThat(result.allocations()).hasSize(2);
        assertThat(result.allocations().get(0).applied()).isEqualByComparingTo("100");
        assertThat(result.allocations().get(0).remainingBalance()).isEqualByComparingTo("0");
        assertThat(result.allocations().get(1).applied()).isEqualByComparingTo("50");
        assertThat(result.remainingDebt()).isEqualByComparingTo("150");
        assertThat(debtService.overview().outstanding()).isEqualByComparingTo("150");
        assertThat(debtService.overview().recovered()).isEqualByComparingTo("150");
    }

    @Test
    void aDebtPaymentCannotExceedWhatIsOwed() {
        invoices.save(overdue("INV-1", ada, 10, "100.00"));

        assertThatThrownBy(() -> paymentService.recordPayment(ada.getId(), new BigDecimal("100.01"),
                PaymentMethod.CASH, null))
                .isInstanceOf(BusinessException.class).hasMessageContaining("more than");
        assertThatThrownBy(() -> paymentService.preview(ada.getId(), BigDecimal.ZERO))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void previewChangesNothing() {
        invoices.save(overdue("INV-1", ada, 10, "100.00"));

        paymentService.preview(ada.getId(), new BigDecimal("40.00"));

        assertThat(debtService.overview().outstanding()).isEqualByComparingTo("100");
        assertThat(debtService.overview().recovered()).isEqualByComparingTo("0");
    }
}
