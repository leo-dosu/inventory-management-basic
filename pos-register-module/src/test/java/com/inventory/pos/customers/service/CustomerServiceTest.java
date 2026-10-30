package com.inventory.pos.customers.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.support.PosDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerServiceTest extends PosDataTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void customerAccountsShowOrdersSpendAndDebt() {
        Invoice paid = overdue("INV-1", ada, 20, "100.00");
        paid.addPayment(new BigDecimal("100.00"), TODAY, PaymentMethod.CASH, null);
        invoices.save(paid);
        invoices.save(overdue("INV-2", ada, 5, "60.00"));

        CustomerAccount account = customerService.accounts("ada").getFirst();

        assertThat(account.orders()).isEqualTo(2);
        assertThat(account.spend()).isEqualByComparingTo("160");
        assertThat(account.debt()).isEqualByComparingTo("60");
        assertThat(account.owes()).isTrue();
        assertThat(customerService.accounts("zzz")).isEmpty();
    }

    @Test
    void newCustomersNeedANameAndSensibleTerms() {
        assertThatThrownBy(() -> customerService.create(new CustomerForm(" ", null, null, null, null, null)))
                .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> customerService.create(new CustomerForm("X", null, null, null, null, 400)))
                .isInstanceOf(BusinessException.class);

        CustomerAccount created = customerService.create(new CustomerForm("New Co", "1", null, null, null, null));
        assertThat(created.termsDays()).isEqualTo(14);
        assertThat(created.debt()).isEqualByComparingTo("0");
    }
}
