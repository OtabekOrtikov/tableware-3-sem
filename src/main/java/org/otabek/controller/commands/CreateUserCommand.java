package org.otabek.controller.commands;

import org.otabek.controller.Request;
import org.otabek.controller.Response;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;
import org.otabek.service.UserService;
import org.otabek.view.MainView;

public class CreateUserCommand implements Command {
    private UserService userService;
    private MainView mainView;

    public CreateUserCommand() {}

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) throws DaoException {
        String username = mainView.requestUsernameForNewUser();
        String password = mainView.requestPasswordForNewUser();
        Role role = mainView.requestRoleForNewUser();
        User newUser = userService.createUser(username, password, role);
        if (newUser != null) {
            return new Response("User created successfully");
        } else {
            return new Response("User already exists");
        }
    }
}
