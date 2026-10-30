
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport("./login-view.css")
public class LoginView extends HorizontalLayout {

    public LoginView() {

        // Main page
        addClassName("login-page");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Left side: Login form
        VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.addClassName("login-panel");
        loginPanel.setPadding(true);
        loginPanel.setSpacing(true);
        loginPanel.setDefaultHorizontalComponentAlignment(
                com.vaadin.flow.component.orderedlayout
                        .FlexComponent.Alignment.STRETCH
        );

        H1 title = new H1("Welcome Back");
        title.addClassName("login-title");

        Paragraph subtitle = new Paragraph(
                "Sign in to your Inventory Management account"
        );
        subtitle.addClassName("login-subtitle");

        EmailField email = new EmailField("Email Address");
        email.setPlaceholder("Enter your email");
        email.setClearButtonVisible(true);
        email.setWidthFull();
        email.addClassName("login-field");

        PasswordField password = new PasswordField("Password");
        password.setPlaceholder("Enter your password");
        password.setWidthFull();
        password.addClassName("login-field");

        Button loginButton = new Button("Sign In");
        loginButton.addClassName("login-button");
        loginButton.setWidthFull();
        loginButton.getStyle().set("background-color", "#2563eb");
        loginButton.getStyle().set("color", "white");

        Paragraph registerText = new Paragraph();

        Anchor registerLink = new Anchor(
                "register",
                "Create an account"
        );

        registerText.add(
                new Span("Don't have an account? "),
                registerLink
        );

        loginPanel.add(
                title,
                subtitle,
                email,
                password,
                loginButton,
                registerText
        );

        // Right side: Full-height image
        VerticalLayout artPanel = new VerticalLayout();
        artPanel.addClassName("login-art-panel");
        artPanel.setPadding(false);
        artPanel.setSpacing(false);

        Image inventoryImage = new Image(
                "/images/inventory-analysis.jpeg",
                "Inventory Analysis"
        );

        inventoryImage.setWidthFull();
        inventoryImage.setHeightFull();
        inventoryImage.getStyle().set("object-fit", "cover");
        inventoryImage.getStyle().set("display", "block");

        artPanel.add(inventoryImage);
        artPanel.setPadding(false);
        artPanel.setSizeFull();

        // Put both panels on the page
        add(loginPanel, artPanel);

        expand(loginPanel);
        expand(artPanel);
    }
}
