package com.inventory.pos;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("styles/base.css")
public class PosApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(PosApplication.class, args);
    }
}
