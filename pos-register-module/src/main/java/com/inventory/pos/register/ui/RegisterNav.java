package com.inventory.pos.register.ui;

import com.inventory.pos.shell.NavEntry;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.stereotype.Component;

@Component
public class RegisterNav implements NavEntry {

    @Override
    public String label() {
        return "POS Register";
    }

    @Override
    public VaadinIcon icon() {
        return VaadinIcon.CART;
    }

    @Override
    public Class<? extends com.vaadin.flow.component.Component> target() {
        return PosView.class;
    }

    @Override
    public int order() {
        return 10;
    }
}
