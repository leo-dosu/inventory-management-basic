package com.inventory.pos.invoices.ui;

import com.inventory.pos.shell.NavEntry;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.stereotype.Component;

@Component
public class InvoicesNav implements NavEntry {

    @Override
    public String label() {
        return "Invoices";
    }

    @Override
    public VaadinIcon icon() {
        return VaadinIcon.CLIPBOARD_TEXT;
    }

    @Override
    public Class<? extends com.vaadin.flow.component.Component> target() {
        return InvoicesView.class;
    }

    @Override
    public int order() {
        return 30;
    }
}
