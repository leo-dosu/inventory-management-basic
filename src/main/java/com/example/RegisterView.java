
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;

@Route("register")
@CssImport("./login-view.css")
public class RegisterView extends HorizontalLayout {

    public RegisterView() {
        addClassName("register-page");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Left side: Full-height image
        VerticalLayout artPanel = new VerticalLayout();
        artPanel.addClassName("register-art-panel");
        artPanel.setPadding(false);
        artPanel.setSpacing(false);
        artPanel.setMargin(false);

        Image inventoryImage = new Image(
                "/images/inventory-analysis.jpeg",
                "Inventory Analysis");

        inventoryImage.setWidth("100%");
        inventoryImage.setHeight("100%");
        inventoryImage.getStyle().set("object-fit", "cover");
        inventoryImage.getStyle().set("display", "block");

        artPanel.add(inventoryImage);
        artPanel.setSizeFull();

        // Right side: Registration form
        VerticalLayout registerPanel = new VerticalLayout();
        registerPanel.addClassName("register-panel");
        registerPanel.setPadding(true);
        registerPanel.setSpacing(true);

        H1 title = new H1("Create Account");
        title.addClassName("register-title");

        Paragraph subtitle = new Paragraph(
                "Create an account to manage your inventory");
        subtitle.addClassName("register-subtitle");

        TextField fullName = new TextField("Full Name");
        fullName.setPlaceholder("Enter your full name");
        fullName.setWidthFull();

        EmailField email = new EmailField("Email Address");
        email.setPlaceholder("Enter your email");
        email.setClearButtonVisible(true);
        email.setWidthFull();

        PasswordField password = new PasswordField("Password");
        password.setPlaceholder("Create a password");
        password.setWidthFull();

        PasswordField confirmPassword = new PasswordField("Confirm Password");
        confirmPassword.setPlaceholder("Confirm your password");
        confirmPassword.setWidthFull();

        Button registerButton = new Button("Create Account");
        registerButton.setWidthFull();
        registerButton.getStyle().set("background-color", "#2563eb");
        registerButton.getStyle().set("color", "white");

        Paragraph loginText = new Paragraph();
        Anchor loginLink = new Anchor("", "Sign in");
        loginText.add(
                new Span("Already have an account? "),
                loginLink);

        registerPanel.add(
                title,
                subtitle,
                fullName,
                email,
                password,
                confirmPassword,
                registerButton,
                loginText);

        add(artPanel, registerPanel);
        expand(artPanel, registerPanel);
    }
}
