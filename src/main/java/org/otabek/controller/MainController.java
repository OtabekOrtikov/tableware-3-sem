package org.otabek.controller;

import org.otabek.entity.Role;
import org.otabek.entity.Tableware;
import org.otabek.exceptions.DaoException;
import org.otabek.service.TablewareService;
import org.otabek.service.UserService;
import org.otabek.view.MainView;
import org.otabek.entity.User;

public class MainController {
    private final UserService userService;
    private final TablewareService tablewareService;
    private final MainView mainView;
    private User loggedInUser;

    public MainController(MainView mainView, TablewareService tablewareService, UserService userService) {
        this.mainView = mainView;
        this.tablewareService = tablewareService;
        this.userService = userService;
    }

    public void run() {
        try {
            authenticateUser();
            if (loggedInUser.getRole() == Role.ADMIN) {
                adminMenu();
            } else {
                userMenu();
            }
        } catch (DaoException e) {
            mainView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void authenticateUser() throws DaoException {
        while (loggedInUser == null) {
            String username = mainView.readUsername();
            String password = mainView.readPassword();
            loggedInUser = userService.authenticate(username, password);
            if (loggedInUser == null) {
                mainView.displayInvalidCredentialsMessage();
            }
        }
    }

    private void adminMenu() throws DaoException {
        while (true) {
            mainView.displayAdminMenu();
            handleCommand(mainView.readCommand(), this::adminMenuActions);
        }
    }

    private void userMenu() throws DaoException {
        while (true) {
            mainView.displayUserMenu();
            handleCommand(mainView.readCommand(), this::userMenuActions);
        }
    }

    private void adminMenuActions(String command) throws DaoException {
        switch (command) {
            case "/users", "1" -> manageUsers();
            case "/items", "2" -> manageItems();
            case "/exit", "3" -> System.exit(0);
            default -> mainView.displayInvalidCommandMessage();
        }
    }

    private void userMenuActions(String command) throws DaoException {
        switch (command) {
            case "/items", "1" -> manageItems();
            case "/exit", "2" -> System.exit(0);
            default -> mainView.displayInvalidCommandMessage();
        }
    }

    private void manageUsers() throws DaoException {
        while (true) {
            mainView.displayManageUsersMenu();
            handleCommand(mainView.readCommand(), this::manageUsersActions);
        }
    }

    private void manageUsersActions(String command) throws DaoException {
        switch (command) {
            case "/list", "1" -> listUsers();
            case "/create", "2" -> createUser();
            case "/delete", "3" -> deleteUser();
            case "/changeAdmin", "4" -> changeAdmin();
            case "/exit", "5" -> {
                return;
            }
            default -> mainView.displayInvalidCommandMessage();
        }
    }

    private void manageItems() throws DaoException {
        while (true) {
            mainView.displayManageItemsMenu();
            handleCommand(mainView.readCommand(), this::manageItemsActions);
        }
    }

    private void manageItemsActions(String command) throws DaoException {
        switch (command) {
            case "/list", "1" -> listItems();
            case "/create", "2" -> createItem();
            case "/update", "3" -> updateItem();
            case "/delete", "4" -> deleteItem();
            case "/back", "5" -> {
                return;
            }
            default -> mainView.displayInvalidCommandMessage();
        }
    }

    private void handleCommand(String command, CommandHandler handler) throws DaoException {
        if (command == null || command.isBlank()) {
            mainView.displayInvalidCommandMessage();
        } else {
            handler.handle(command);
        }
    }

    private void listUsers() throws DaoException {
        userService.listAllUsers().forEach(user -> mainView.displayUser(user.toString()));
    }

    private void createUser() throws DaoException {
        String username = mainView.requestUsernameForNewUser();
        String password = mainView.requestPasswordForNewUser();
        Role role = mainView.requestRoleForNewUser();
        try {
            userService.createUser(username, password, role);
            mainView.displaySuccessMessage("User created successfully.");
        } catch (DaoException e) {
            mainView.displayErrorMessage("Error creating user: " + e.getMessage());
        }
    }

    private void deleteUser() throws DaoException {
        int id = mainView.requestUserIdForDeletion();
        if (userService.deleteUser(id)) {
            mainView.displaySuccessMessage("User deleted successfully.");
        } else {
            mainView.displayErrorMessage("User not found.");
        }
    }

    private void listItems() throws DaoException {
        tablewareService.getAll().forEach(item -> mainView.displayItem(item.toString()));
    }

    private void createItem() throws DaoException {
        String itemType = mainView.requestItemType();
        try {
            Tableware newItem = mainView.requestItemDetails(itemType);
            tablewareService.createTableware(newItem);
            mainView.displaySuccessMessage(itemType + " created successfully.");
        } catch (IllegalArgumentException e) {
            mainView.displayErrorMessage("Invalid item type: " + e.getMessage());
        }
    }

    private void updateItem() throws DaoException {
        int id = mainView.requestItemIdForUpdate();
        if (tablewareService.getByID(id) == null) {
            mainView.displayErrorMessage("Item not found.");
        } else {
            String column = mainView.requestColumnForUpdate();
            String value = mainView.requestNewValueForUpdate();
            tablewareService.updateTableware(column, value, id);
            mainView.displaySuccessMessage("Item updated successfully.");
        }
    }

    private void deleteItem() throws DaoException {
        int id = mainView.requestItemIdForDeletion();
        if (tablewareService.deleteTableware(id)) {
            mainView.displaySuccessMessage("Item deleted successfully.");
        } else {
            mainView.displayErrorMessage("Item not found.");
        }
    }

    private void changeAdmin() throws DaoException {
        int newAdminId = mainView.requestUserIdForRoleChange();
        try {
            userService.changeAdmin(newAdminId);
            mainView.displaySuccessMessage("Admin role successfully transferred to user ID: " + newAdminId);
        } catch (DaoException e) {
            mainView.displayErrorMessage("Error changing admin: " + e.getMessage());
        }
    }

    @FunctionalInterface
    interface CommandHandler {
        void handle(String command) throws DaoException;
    }
}