package com.inventory.pos.shell;

import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Value;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The frame from the Figma design: a header with the menu button, search, bell and profile on a white bar, and a
 * narrow dark sidebar with icon-over-label items.
 *
 * <p>The sidebar is built from every {@link NavEntry} bean in the application, so a new screen appears here by adding
 * its own entry, never by editing this class.
 */
public class MainLayout extends Div implements RouterLayout {

    /** Lets any screen ask the sidebar to recount its badges after something changes. */
    public interface BadgeRefresher extends Runnable {
    }

    private final List<NavEntry> entries;
    private final Map<NavEntry, Span> badges = new LinkedHashMap<>();
    private final Span bellDot = new Span();
    private final Div content = new Div();

    public MainLayout(List<NavEntry> entries, @Value("${pos.business-name}") String businessName) {
        this.entries = entries.stream().sorted(Comparator.comparingInt(NavEntry::order)).toList();
        addClassName("app");
        content.addClassName("content");

        Div body = new Div(sidebar(), content);
        body.addClassName("app-body");
        add(header(businessName), body);

        updateBadges();
        ComponentUtil.setData(UI.getCurrent(), BadgeRefresher.class, (BadgeRefresher) this::updateBadges);
    }

    /** Call after anything that changes a sidebar badge, such as who owes money. */
    public static void refreshBadges() {
        BadgeRefresher refresher = ComponentUtil.getData(UI.getCurrent(), BadgeRefresher.class);
        if (refresher != null) {
            refresher.run();
        }
    }

    private void updateBadges() {
        boolean anyAlert = false;
        for (Map.Entry<NavEntry, Span> badge : badges.entrySet()) {
            long count = badge.getKey().badge();
            badge.getValue().setText(String.valueOf(count));
            badge.getValue().setVisible(count > 0);
            anyAlert |= count > 0;
        }
        bellDot.setVisible(anyAlert);
    }

    private Div header(String businessName) {
        Button menu = Panels.iconButton(VaadinIcon.MENU, "Toggle menu");
        menu.addClassName("menu-button");
        menu.addClickListener(e -> getClassNames().set("sidebar-hidden", !getClassNames().contains("sidebar-hidden")));

        Span mark = new Span("QP");
        mark.addClassName("brand-mark");
        Span name = new Span(businessName);
        name.addClassName("brand-name");
        Div brand = new Div(mark, name);
        brand.addClassName("brand");

        Button newSale = Panels.button("New Sale", VaadinIcon.CART, "btn-primary");
        newSale.addClassName("new-sale");
        newSale.addClickListener(e -> UI.getCurrent().navigate(Routes.REGISTER));

        Button search = Panels.iconButton(VaadinIcon.SEARCH, "Find an invoice");
        search.addClassName("search-button");
        search.addClickListener(e -> UI.getCurrent().navigate(Routes.INVOICES));

        bellDot.addClassName("bell-dot");
        Button bell = Panels.iconButton(VaadinIcon.BELL, "Things that need attention");
        bell.addClassName("bell-button");
        bell.getElement().appendChild(bellDot.getElement());
        bell.addClickListener(e -> entries.stream().filter(entry -> entry.badge() > 0).findFirst()
                .ifPresent(entry -> UI.getCurrent().navigate(entry.target())));

        Span avatar = new Span("C");
        avatar.addClassName("avatar");
        Span role = new Span("Cashier");
        role.addClassName("profile-name");
        Div profile = new Div(avatar, role);
        profile.addClassName("profile");

        Div left = new Div(menu, brand);
        left.addClassName("header-left");
        Div actions = new Div(newSale, search, bell, profile);
        actions.addClassName("header-actions");

        Div header = new Div(left, actions);
        header.addClassName("app-header");
        return header;
    }

    private Nav sidebar() {
        Nav nav = new Nav();
        nav.addClassName("sidebar");
        for (NavEntry entry : entries) {
            RouterLink link = new RouterLink(entry.target());
            link.add(new Icon(entry.icon()), new Span(entry.label()));
            Span badge = new Span();
            badge.addClassName("nav-badge");
            badges.put(entry, badge);
            link.add(badge);
            link.addClassName("nav-item");
            link.setHighlightCondition(HighlightConditions.sameLocation());
            nav.add(link);
        }
        return nav;
    }

    @Override
    public void showRouterLayoutContent(HasElement routeContent) {
        content.removeAll();
        if (routeContent != null) {
            content.getElement().appendChild(routeContent.getElement());
        }
    }
}
