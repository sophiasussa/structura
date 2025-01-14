package com.example.application.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;
import com.example.application.controller.UserController;
import com.example.application.model.User;

@Route("login")
public class UserView extends Div {

    private UserController userController = new UserController();

    public UserView() {
        User currentUser = (User) UI.getCurrent().getSession().getAttribute(User.class);
        if (currentUser != null) {
            getUI().ifPresent(ui -> ui.navigate(""));
            return;
        }

        getStyle()
            .set("background-color", "var(--lumo-contrast-20pct)")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("justify-content", "center")
            .set("align-items", "center")
            .set("height", "100vh")
            .set("padding", "var(--lumo-space-l)");

        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.addLoginListener(e -> {
            String username = e.getUsername();
            String password = e.getPassword();

            User user = userController.login(username, password);
            if (user != null) {
                getUI().ifPresent(ui -> {
                    ui.getSession().setAttribute(User.class, user);
                    ui.navigate("");
                });
            } else {
                loginForm.setError(true);
            }
        });

        loginForm.getElement().setAttribute("no-autofocus", "");

        loginForm.getStyle()
            .set("width", "100%")
            .set("max-width", "400px");

        Div loginJunto = new Div(loginForm);
        loginJunto.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("align-items", "center");

        add(loginJunto);
    }
}