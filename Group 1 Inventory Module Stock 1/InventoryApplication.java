package com.group3.inventory;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.server.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Vaadin 25 loads no theme by default: Lumo gives the standard look, styles.css adds the Figma colours.
@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("styles.css")
public class InventoryApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}
