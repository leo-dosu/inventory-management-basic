package com.inventory.pos.inventory.ui;

import com.inventory.pos.shell.NavEntry;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.stereotype.Component;

@Component
public class InventoryNav implements NavEntry {

    @Override
    public String label() {
        return "Inventory";
    }

    @Override
    public VaadinIcon icon() {
        return VaadinIcon.CUBES;
    }

    @Override
    public Class<? extends com.vaadin.flow.component.Component> target() {
        return InventoryView.class;
    }

    @Override
    public int order() {
        return 50;
    }
}
