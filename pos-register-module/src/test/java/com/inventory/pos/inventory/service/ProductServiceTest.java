package com.inventory.pos.inventory.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.invoices.service.InvoiceDetail;
import com.inventory.pos.invoices.service.InvoiceService;
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

class ProductServiceTest extends PosDataTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private InvoiceService invoiceService;

    @Test
    void productSkusMustBeUniqueButCanBeKeptWhenEditing() {
        assertThatThrownBy(() -> productService.save(new ProductForm(null, "MOU-1", "Another", "X",
                BigDecimal.TEN, 1, "pc"))).isInstanceOf(BusinessException.class).hasMessageContaining("SKU");

        Product edited = productService.save(new ProductForm(mouse.getId(), "MOU-1", "Mouse Pro", "Accessories",
                new BigDecimal("120"), 7, "pc"));
        assertThat(edited.getName()).isEqualTo("Mouse Pro");
        assertThat(edited.getUnitPrice()).isEqualByComparingTo("120");
        assertThat(edited.getStock()).isEqualTo(7);
    }

    @Test
    void productSearchAndCategoryFilter() {
        assertThat(productService.list("mou", null)).extracting(Product::getSku).containsExactly("MOU-1");
        assertThat(productService.list("cab-1", null)).extracting(Product::getSku).containsExactly("CAB-1");
        assertThat(productService.list(null, "Accessories")).hasSize(2);
        assertThat(productService.list(null, "Nothing")).isEmpty();
        assertThat(productService.categories()).containsExactly("Accessories");
    }

    @Test
    void productsCanBeDeletedAndOldInvoicesKeepTheirLines() {
        InvoiceDetail sale = checkoutService.checkout(new CheckoutRequest(null,
                List.of(new CartLine(cable.getId(), null, 1, null)), BigDecimal.ZERO, new BigDecimal("55.00"),
                PaymentMethod.CASH));

        productService.delete(cable.getId());

        assertThat(products.findById(cable.getId())).isEmpty();
        assertThat(invoiceService.detail(sale.summary().id()).lines().getFirst().description()).isEqualTo("Cable");
    }
}
