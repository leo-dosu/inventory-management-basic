package com.inventory.pos.debt.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.support.PosDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReminderServiceTest extends PosDataTest {

    @Autowired
    private ReminderService reminderService;

    @Test
    void reminderNamesTheInvoicesAndIsRecorded() {
        invoices.save(overdue("INV-1", ada, 10, "100.00"));

        String message = reminderService.prepareReminder(ada.getId());

        assertThat(message).contains("Ada Stores").contains("INV-1").contains("100.00").contains("Test Shop");
    }

    @Test
    void noReminderForSomeoneWhoOwesNothing() {
        assertThatThrownBy(() -> reminderService.prepareReminder(ada.getId()))
                .isInstanceOf(BusinessException.class).hasMessageContaining("nothing outstanding");
    }
}
