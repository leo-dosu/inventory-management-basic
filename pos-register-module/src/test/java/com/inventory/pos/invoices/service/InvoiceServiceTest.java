package com.inventory.pos.invoices.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.InvoiceStatus;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.register.service.CartLine;
import com.inventory.pos.register.service.CheckoutRequest;
import com.inventory.pos.register.service.CheckoutService;
import com.inventory.pos.support.PosDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvoiceServiceTest extends PosDataTest {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CheckoutService checkoutService;

    private InvoiceDetail creditSaleOfOneMouse() {
        return checkoutService.checkout(new CheckoutRequest(ada.getId(),
                List.of(new CartLine(mouse.getId(), null, 1, null)), BigDecimal.ZERO, BigDecimal.ZERO,
                PaymentMethod.CASH));
    }

    @Test
    void paymentsReduceTheBalanceUntilTheInvoiceIsPaid() {
        Long id = creditSaleOfOneMouse().summary().id();

        InvoiceDetail half = invoiceService.recordPayment(id, new BigDecimal("60.00"), PaymentMethod.CASH, null);
        assertThat(half.summary().balance()).isEqualByComparingTo("50");
        assertThat(half.summary().status()).isEqualTo(InvoiceStatus.PARTIAL);

        InvoiceDetail full = invoiceService.recordPayment(id, new BigDecimal("50.00"), PaymentMethod.TRANSFER, "Rest");
        assertThat(full.summary().status()).isEqualTo(InvoiceStatus.PAID);
        assertThat(full.payments()).hasSize(2);
    }

    @Test
    void paymentsMustBePositiveAndNotExceedTheBalance() {
        Long id = creditSaleOfOneMouse().summary().id();

        assertThatThrownBy(() -> invoiceService.recordPayment(id, BigDecimal.ZERO, null, null))
                .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> invoiceService.recordPayment(id, new BigDecimal("110.01"), null, null))
                .isInstanceOf(BusinessException.class).hasMessageContaining("more than the balance");
    }

    @Test
    void anInvoicePastItsDueDateIsOverdueEvenIfPartlyPaid() {
        Invoice late = overdue("INV-X-1", ada, 10, "100.00");
        late.addPayment(new BigDecimal("20.00"), TODAY.minusDays(12), PaymentMethod.CASH, null);
        invoices.save(late);

        InvoiceSummary summary = invoiceService.list(null, InvoiceFilter.OVERDUE).getFirst();

        assertThat(summary.status()).isEqualTo(InvoiceStatus.OVERDUE);
        assertThat(summary.daysOverdue()).isEqualTo(10);
        assertThat(invoiceService.counts().get(InvoiceFilter.OVERDUE)).isEqualTo(1);
        assertThat(invoiceService.counts().get(InvoiceFilter.ALL)).isEqualTo(1);
    }

    @Test
    void invoicesCanBeSearchedByNumberOrCustomer() {
        invoices.save(overdue("INV-A-1", ada, 5, "10.00"));
        invoices.save(overdue("INV-B-2", ada, 5, "10.00"));

        assertThat(invoiceService.list("a-1", InvoiceFilter.ALL)).hasSize(1);
        assertThat(invoiceService.list("ada", InvoiceFilter.ALL)).hasSize(2);
        assertThat(invoiceService.list("nobody", InvoiceFilter.ALL)).isEmpty();
    }
}
