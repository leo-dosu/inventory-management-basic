package com.inventory.pos.debt.service;

import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.support.PosDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DebtServiceTest extends PosDataTest {

    @Autowired
    private DebtService debtService;

    @Test
    void overviewCountsOnlyPeopleWhoStillOwe() {
        Customer settled = customers.save(new Customer("Settled Ltd", null, null, null, null, 14));
        Invoice paid = overdue("INV-PAID", settled, 20, "50.00");
        paid.addPayment(new BigDecimal("50.00"), TODAY.minusDays(1), PaymentMethod.CASH, null);
        invoices.save(paid);
        invoices.save(overdue("INV-OWED", ada, 4, "80.00"));

        DebtOverview overview = debtService.overview();

        assertThat(overview.debtorAccounts()).isEqualTo(1);
        assertThat(overview.openInvoices()).isEqualTo(1);
        assertThat(overview.overdueArrears()).isEqualByComparingTo("80");
        assertThat(overview.recovered()).isEqualByComparingTo("50");
    }

    @Test
    void debtorFiltersSeparateOverdueFromDueSoon() {
        Customer soon = customers.save(new Customer("Soon Ltd", null, null, null, null, 14));
        Invoice dueInThreeDays = new Invoice("INV-SOON", soon, TODAY.minusDays(11), TODAY.plusDays(3), BigDecimal.ZERO);
        dueInThreeDays.addLine(null, null, "Goods", 1, new BigDecimal("500.00"));
        dueInThreeDays.applyDiscount(BigDecimal.ZERO);
        invoices.save(dueInThreeDays);
        invoices.save(overdue("INV-LATE", ada, 9, "100.00"));

        assertThat(debtService.debtors(null, DebtFilter.ALL)).extracting(Debtor::name)
                .containsExactly("Soon Ltd", "Ada Stores");
        assertThat(debtService.debtors(null, DebtFilter.OVERDUE)).extracting(Debtor::name).containsExactly("Ada Stores");
        assertThat(debtService.debtors(null, DebtFilter.DUE_SOON)).extracting(Debtor::name).containsExactly("Soon Ltd");
        assertThat(debtService.debtors("late", DebtFilter.ALL)).extracting(Debtor::name).containsExactly("Ada Stores");
        assertThat(debtService.debtors(null, DebtFilter.OVERDUE).getFirst().maxDaysOverdue()).isEqualTo(9);
    }
}
