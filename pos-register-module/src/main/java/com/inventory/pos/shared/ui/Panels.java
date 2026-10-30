package com.inventory.pos.shared.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/** Small building blocks every screen uses, so they all look the same. */
public final class Panels {

    private Panels() {
    }

    /** Title and subtitle on the left, action buttons on the right. */
    public static Div pageHeading(String title, String subtitle, Component... actions) {
        Paragraph sub = new Paragraph(subtitle);
        sub.addClassName("page-subtitle");
        Div text = new Div(new H1(title), sub);
        Div actionBar = new Div(actions);
        actionBar.addClassName("page-actions");
        Div heading = new Div(text, actionBar);
        heading.addClassName("page-heading");
        return heading;
    }

    public static Span badge(String text, String tone) {
        Span badge = new Span(text);
        badge.addClassNames("badge", "badge-" + tone);
        return badge;
    }

    public static Div emptyState(VaadinIcon icon, String title, String hint) {
        Paragraph heading = new Paragraph(title);
        heading.addClassName("empty-title");
        Paragraph text = new Paragraph(hint);
        text.addClassName("empty-hint");
        Div state = new Div(new Icon(icon), heading, text);
        state.addClassName("empty-state");
        return state;
    }

    public static H3 sectionTitle(String text) {
        H3 title = new H3(text);
        title.addClassName("section-title");
        return title;
    }

    /** A button styled by class name: "btn-primary", "btn-secondary", "btn-danger" or "btn-ghost". */
    public static Button button(String text, VaadinIcon icon, String style) {
        Button button = icon == null ? new Button(text) : new Button(text, new Icon(icon));
        button.addClassNames("btn", style);
        return button;
    }

    public static Button iconButton(VaadinIcon icon, String label) {
        Button button = new Button(new Icon(icon));
        button.addClassName("icon-button");
        button.getElement().setAttribute("aria-label", label);
        button.getElement().setAttribute("title", label);
        return button;
    }

    /** A label above a value, used in dialogs and detail rows. */
    public static Div detailRow(String label, Component value) {
        Span name = new Span(label);
        name.addClassName("detail-label");
        Div row = new Div(name, value);
        row.addClassName("detail-row");
        return row;
    }

    public static Div detailRow(String label, String value) {
        Span text = new Span(value);
        text.addClassName("detail-value");
        return detailRow(label, text);
    }
}
