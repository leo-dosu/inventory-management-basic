package com.group3.inventory;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Inventory Management")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {
        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");

        add(loginForm);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}