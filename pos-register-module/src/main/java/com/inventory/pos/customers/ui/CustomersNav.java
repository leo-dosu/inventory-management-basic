package com.inventory.pos.customers.ui;

import com.inventory.pos.shell.NavEntry;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.stereotype.Component;

@Component
public class CustomersNav implements NavEntry {

    @Override
    public String label() {
        return "Customers";
    }

    @Override
    public VaadinIcon icon() {
        return VaadinIcon.USERS;
    }

    @Override
    public Class<? extends com.vaadin.flow.component.Component> target() {
        return CustomersView.class;
    }

    @Override
    public int order() {
        return 40;
    }
}
