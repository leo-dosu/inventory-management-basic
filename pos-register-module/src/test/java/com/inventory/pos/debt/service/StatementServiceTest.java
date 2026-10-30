package com.inventory.pos.debt.service;

import com.inventory.pos.support.PosDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class StatementServiceTest extends PosDataTest {

    @Autowired
    private StatementService statementService;

    @Test
    void statementTotalsAddUp() {
        invoices.save(overdue("INV-1", ada, 20, "100.00"));
        invoices.save(overdue("INV-2", ada, 5, "60.00"));

        Statement statement = statementService.statement(ada.getId());

        assertThat(statement.customerName()).isEqualTo("Ada Stores");
        assertThat(statement.termsDays()).isEqualTo(14);
        assertThat(statement.invoices()).hasSize(2);
        assertThat(statement.totalBilled()).isEqualByComparingTo("160");
        assertThat(statement.totalOutstanding()).isEqualByComparingTo("160");
        assertThat(statement.asOf()).isEqualTo(TODAY);
    }
}
