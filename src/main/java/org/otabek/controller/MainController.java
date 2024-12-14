package org.otabek.controller;

import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;
import org.otabek.exceptions.DaoException;
import org.otabek.service.TablewareService;
import org.otabek.service.UserService;
import org.otabek.view.MainView;

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

    public void run() throws DaoException {
        while (loggedInUser == null) {
            mainView.displayLoginPrompt();
            String username = mainView.readUsername();
            String password = mainView.readPassword();

            User user = userService.authenticate(username, password);
            if (user != null) {
                loggedInUser = user;
            } else {
                mainView.displayInvalidCredentialsMessage();
            }
        }

        if (loggedInUser.getRole() == Role.ADMIN) {
            adminMenu();
        } else {
            userMenu();
        }
    }

    private void adminMenu() throws DaoException {
        while (true) {
            mainView.displayAdminMenu();
            String command = mainView.readCommand();
            switch (command) {
                case "/users", "1" -> manageUser();
                case "/items", "2" -> manageItems();
                case "/exit", "3" -> System.exit(0);
                default -> mainView.displayInvalidCommandMessage();
            }
        }
    }

    private void userMenu() throws DaoException {
        while (true) {
            mainView.displayUserMenu();
            String command = mainView.readCommand();
            switch (command) {
                case "/items", "1" -> manageItems();
                case "/exit", "2" -> System.exit(0);
                default -> mainView.displayInvalidCommandMessage();
            }
        }
    }

    private void manageUser() throws DaoException {
        while (true) {
            mainView.displayManageUsersMenu();
            String command = mainView.readCommand();
            switch (command) {
                case "/list", "1" -> {
                    System.out.println("| id | username | role |");
                    userService.listAllUsers().forEach(u -> mainView.displayMessage(u.toString()));
                }
                case "/create", "2" -> createUser();
                case "/delete", "3" -> deleteUser();
                case "/exit", "4" -> {
                    return;
                }
                default -> mainView.displayInvalidCommandMessage();
            }
        }
    }

    private void createUser() throws DaoException {
        String username = mainView.prompt("Enter new user's username:");
        String password = mainView.prompt("Enter new user's password:");
        String roleStr = mainView.prompt("Enter new user's role (USER or ADMIN):");
        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            mainView.displayMessage("Invalid role. Please try again.");
            return;
        }
        userService.createUser(username, password, role);
        mainView.displayMessage("User created successfully.");
    }

    private void deleteUser() throws DaoException {
        int id = Integer.parseInt(mainView.prompt("Enter user's id:"));
        if (userService.deleteUser(id)) {
            mainView.displayMessage("User deleted successfully.");
        } else {
            mainView.displayMessage("User not found.");
        }
    }

    private void manageItems() throws DaoException {
        while (true) {
            mainView.displayManageItemsMenu();
            String command = mainView.readCommand();
            switch (command) {
                case "/list", "1" -> {
                    System.out.println("| id | name | width | color | price | another field |");
                    tablewareService.getAll().forEach(i -> mainView.displayMessage(i.toString()));
                }
                case "/create", "2" -> createItem();
                case "/update", "3" -> updateItem();
                case "/delete", "4" -> deleteItem();
                case "/exit", "5" -> {
                    return;
                }
                default -> mainView.displayInvalidCommandMessage();
            }
        }
    }

    private void createItem() throws DaoException {
        mainView.displayCreateItemMenu();
        String itemType = mainView.readLine();
        String name = mainView.prompt("Enter item's name:");
        float width = Float.parseFloat(mainView.prompt("Enter item's width:"));
        String color = mainView.prompt("Enter item's color:");
        float price = Float.parseFloat(mainView.prompt("Enter item's price:"));

        switch (itemType.toLowerCase()) {
            case "plate", "1" -> {
                float radius = Float.parseFloat(mainView.prompt("Enter plate's radius:"));
                Plate plate = new Plate(0, name, width, color, price, radius);
                tablewareService.createTableware(plate);
                mainView.displayMessage("Plate created successfully.");
            }
            case "cup", "2" -> {
                String category = mainView.prompt("Enter cup's category:");
                Cup cup = new Cup(0, name, width, color, price, category);
                tablewareService.createTableware(cup);
                mainView.displayMessage("Cup created successfully.");
            }
            case "teapot", "3" -> {
                String style = mainView.prompt("Enter teapot's style:");
                Teapot teapot = new Teapot(0, name, width, color, price, style);
                tablewareService.createTableware(teapot);
                mainView.displayMessage("Teapot created successfully.");
            }
            default -> mainView.displayMessage("Invalid item type. Please try again.");
        }
    }

    private void updateItem() throws DaoException {
        mainView.displayMessage("If you want to back, enter 0.");
        int id = Integer.parseInt(mainView.prompt("Enter item's id:"));
        if (id == 0) {
            return;
        }
        if (tablewareService.getByID(id) == null) {
            mainView.displayMessage("Item not found.");
        } else {
            String column = mainView.prompt("Enter column name to update:");
            String value = mainView.prompt("Enter new value:");
            tablewareService.updateTableware(column, value, id);
            mainView.displayMessage("Item updated successfully.");
        }
    }

    private void deleteItem() throws DaoException {
        int id = Integer.parseInt(mainView.prompt("Enter item's id:"));
        if (tablewareService.deleteTableware(id)) {
            mainView.displayMessage("Item deleted successfully.");
        } else {
            mainView.displayMessage("Item not found.");
        }
    }
}