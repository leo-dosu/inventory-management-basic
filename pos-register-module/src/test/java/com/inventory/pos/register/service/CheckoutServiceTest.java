package com.inventory.pos.register.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.InvoiceStatus;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.invoices.service.InvoiceDetail;
import com.inventory.pos.invoices.service.InvoiceSummary;
import com.inventory.pos.support.PosDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CheckoutServiceTest extends PosDataTest {

    @Autowired
    private CheckoutService checkoutService;

    @Test
    void walkInSaleTakesStockAndIsPaidInFull() {
        InvoiceDetail sale = checkoutService.checkout(new CheckoutRequest(null,
                List.of(new CartLine(mouse.getId(), null, 2, null)), BigDecimal.ZERO,
                new BigDecimal("220.00"), PaymentMethod.CASH));

        assertThat(sale.summary().number()).isEqualTo("INV-2026-001");
        assertThat(sale.subtotal()).isEqualByComparingTo("200");
        assertThat(sale.tax()).isEqualByComparingTo("20");
        assertThat(sale.summary().total()).isEqualByComparingTo("220");
        assertThat(sale.summary().status()).isEqualTo(InvoiceStatus.PAID);
        assertThat(sale.summary().customerName()).isEqualTo(InvoiceSummary.WALK_IN);
        assertThat(products.findById(mouse.getId()).orElseThrow().getStock()).isEqualTo(8);
    }

    @Test
    void invoiceNumbersCountUpWithinTheYear() {
        CheckoutRequest request = new CheckoutRequest(null, List.of(new CartLine(mouse.getId(), null, 1, null)),
                BigDecimal.ZERO, new BigDecimal("110.00"), PaymentMethod.CASH);

        assertThat(checkoutService.checkout(request).summary().number()).isEqualTo("INV-2026-001");
        assertThat(checkoutService.checkout(request).summary().number()).isEqualTo("INV-2026-002");
    }

    @Test
    void walkInCannotOweMoney() {
        assertThatThrownBy(() -> checkoutService.checkout(new CheckoutRequest(null,
                List.of(new CartLine(mouse.getId(), null, 1, null)), BigDecimal.ZERO,
                new BigDecimal("50.00"), PaymentMethod.CASH)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("paid in full");
    }

    @Test
    void customerCanPayPartAndOwesTheRestUntilTheTermsRunOut() {
        InvoiceDetail sale = checkoutService.checkout(new CheckoutRequest(ada.getId(),
                List.of(new CartLine(mouse.getId(), null, 1, null), new CartLine(cable.getId(), null, 1, null)),
                BigDecimal.ZERO, new BigDecimal("65.00"), PaymentMethod.TRANSFER));

        assertThat(sale.summary().total()).isEqualByComparingTo("165");
        assertThat(sale.summary().balance()).isEqualByComparingTo("100");
        assertThat(sale.summary().status()).isEqualTo(InvoiceStatus.PARTIAL);
        assertThat(sale.summary().dueOn()).isEqualTo(TODAY.plusDays(14));
        assertThat(sale.payments()).hasSize(1);
    }

    @Test
    void creditSaleWithNothingPaidIsUnpaid() {
        InvoiceDetail sale = checkoutService.checkout(new CheckoutRequest(ada.getId(),
                List.of(new CartLine(cable.getId(), null, 2, null)), BigDecimal.ZERO, BigDecimal.ZERO,
                PaymentMethod.CASH));

        assertThat(sale.summary().status()).isEqualTo(InvoiceStatus.UNPAID);
        assertThat(sale.payments()).isEmpty();
    }

    @Test
    void discountComesOffBeforeTax() {
        InvoiceDetail sale = checkoutService.checkout(new CheckoutRequest(null,
                List.of(new CartLine(mouse.getId(), null, 2, null)), new BigDecimal("20.00"),
                new BigDecimal("198.00"), PaymentMethod.CASH));

        assertThat(sale.tax()).isEqualByComparingTo("18");
        assertThat(sale.summary().total()).isEqualByComparingTo("198");
    }

    @Test
    void cannotSellMoreThanIsInStock() {
        assertThatThrownBy(() -> checkoutService.checkout(new CheckoutRequest(ada.getId(),
                List.of(new CartLine(mouse.getId(), null, 11, null)), BigDecimal.ZERO, BigDecimal.ZERO,
                PaymentMethod.CASH)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Only 10 of Mouse in stock");
    }

    @Test
    void customItemsCarryTheirOwnPriceAndTouchNoStock() {
        InvoiceDetail sale = checkoutService.checkout(new CheckoutRequest(null,
                List.of(new CartLine(null, "Delivery", 1, new BigDecimal("500.00"))), BigDecimal.ZERO,
                new BigDecimal("550.00"), PaymentMethod.CASH));

        assertThat(sale.lines()).singleElement().satisfies(line -> {
            assertThat(line.description()).isEqualTo("Delivery");
            assertThat(line.sku()).isNull();
        });
        assertThat(products.findAll()).extracting(Product::getStock).containsExactlyInAnyOrder(10, 5);
    }

    @Test
    void emptyCartAndBadDiscountAreRejected() {
        assertThatThrownBy(() -> checkoutService.checkout(new CheckoutRequest(null, List.of(), null, null, null)))
                .isInstanceOf(BusinessException.class).hasMessageContaining("empty");
        assertThatThrownBy(() -> checkoutService.checkout(new CheckoutRequest(null,
                List.of(new CartLine(mouse.getId(), null, 1, null)), new BigDecimal("101.00"), null, null)))
                .isInstanceOf(BusinessException.class).hasMessageContaining("discount");
    }
}
