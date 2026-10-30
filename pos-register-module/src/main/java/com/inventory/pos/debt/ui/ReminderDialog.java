package com.inventory.pos.debt.ui;

import com.inventory.pos.shared.ui.Notify;
import com.inventory.pos.shared.ui.Panels;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;

/** Shows the reminder wording so it can be copied into WhatsApp, SMS or email. This app sends nothing itself. */
public final class ReminderDialog {

    private ReminderDialog() {
    }

    public static void open(String customerName, String message) {
        Dialog dialog = new Dialog();
        dialog.addClassName("pos-dialog");
        dialog.setWidth("520px");
        dialog.setHeaderTitle("Reminder for " + customerName);

        TextArea text = new TextArea();
        text.setValue(message);
        text.setReadOnly(true);
        text.setWidthFull();
        text.setMinHeight("150px");
        Paragraph hint = new Paragraph("The reminder is logged, but nothing has been sent. Copy it into your own "
                + "WhatsApp, SMS or email.");
        hint.addClassName("form-hint");
        dialog.add(new Div(text, hint));

        Button close = Panels.button("Close", null, "btn-secondary");
        close.addClickListener(e -> dialog.close());
        Button copy = Panels.button("Copy message", VaadinIcon.COPY_O, "btn-primary");
        copy.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", message);
            Notify.success("Reminder copied.");
        });
        dialog.getFooter().add(close, copy);
        dialog.open();
    }
}
