package com.group3.inventory;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

/** Top bar (menu button, search, bell, user) and the blue side menu from the Figma design. */
public class MainLayout extends AppLayout {

    public MainLayout() {
        Span avatar = new Span();
        avatar.addClassName("avatar");
        Span name = new Span("Ann Lee");
        name.addClassName("profile-name");

        HorizontalLayout profile = new HorizontalLayout(
                VaadinIcon.SEARCH.create(), VaadinIcon.BELL.create(), avatar, name);
        profile.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout bar = new HorizontalLayout(new DrawerToggle(), profile);
        bar.addClassName("topbar");
        bar.setWidthFull();
        bar.setAlignItems(FlexComponent.Alignment.CENTER);
        bar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        addToNavbar(bar);

        // Only "In Stock" is built; the others are placeholders for the group's other modules.
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem("Dashboard", (String) null, VaadinIcon.DASHBOARD.create()),
                new SideNavItem("In Stock", "", VaadinIcon.CUBES.create()),
                new SideNavItem("Products", (String) null, VaadinIcon.TAGS.create()),
                new SideNavItem("Sales", (String) null, VaadinIcon.CART.create()),
                new SideNavItem("Orders", (String) null, VaadinIcon.LIST.create()),
                new SideNavItem("Users", (String) null, VaadinIcon.USERS.create()));
        addToDrawer(nav);
        setPrimarySection(Section.DRAWER);
    }
}
