package com.inventory.pos.shell;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * One item in the sidebar. To add a screen to the menu, write a small Spring {@code @Component} that implements this
 * (see {@code register/ui/RegisterNav.java}). The shell finds it by itself, so no shared file needs editing.
 */
public interface NavEntry {

    /** The text under the icon. */
    String label();

    VaadinIcon icon();

    /** The screen to open. */
    Class<? extends Component> target();

    /** Position in the sidebar, lowest first. Existing entries use 10 to 50; take a free number after that. */
    int order();

    /** A red number on the item, such as how many customers owe money. Zero hides it. */
    default long badge() {
        return 0;
    }
}
