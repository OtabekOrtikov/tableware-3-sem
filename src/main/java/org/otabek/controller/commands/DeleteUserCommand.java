package org.otabek.controller.commands;

import org.otabek.controller.Request;
import org.otabek.controller.Response;
import org.otabek.exceptions.DaoException;
import org.otabek.service.UserService;
import org.otabek.view.MainView;

public class DeleteUserCommand implements Command {
    private UserService userService;
    private MainView mainView;

    public DeleteUserCommand() {}

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) throws DaoException {
        int userId = mainView.requestUserIdForDeletion();
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return new Response("User deleted successfully");
        } else {
            return new Response("User not found");
        }
    }
}
