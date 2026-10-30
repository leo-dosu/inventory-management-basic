package com.inventory.pos.shared.ui;

import com.vaadin.flow.component.notification.Notification;

public final class Notify {

    private Notify() {
    }

    public static void success(String message) {
        Notification notification = Notification.show(message, 4500, Notification.Position.BOTTOM_END);
        notification.addThemeName("success");
    }

    public static void error(String message) {
        Notification notification = Notification.show(message, 7000, Notification.Position.BOTTOM_END);
        notification.addThemeName("error");
    }
}
