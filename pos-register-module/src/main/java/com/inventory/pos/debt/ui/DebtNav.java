package com.inventory.pos.debt.ui;

import com.inventory.pos.debt.service.DebtService;
import com.inventory.pos.shell.NavEntry;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.stereotype.Component;

@Component
public class DebtNav implements NavEntry {

    private final DebtService debts;

    public DebtNav(DebtService debts) {
        this.debts = debts;
    }

    @Override
    public String label() {
        return "Debt Recovery";
    }

    @Override
    public VaadinIcon icon() {
        return VaadinIcon.COIN_PILES;
    }

    @Override
    public Class<? extends com.vaadin.flow.component.Component> target() {
        return DebtView.class;
    }

    @Override
    public int order() {
        return 20;
    }

    /** How many customers owe money. */
    @Override
    public long badge() {
        return debts.overview().debtorAccounts();
    }
}
