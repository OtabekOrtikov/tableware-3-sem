package org.otabek.controller;

import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;
import org.otabek.service.TablewareService;
import org.otabek.service.UserService;
import org.otabek.view.MainView;

import java.util.Scanner;

public class MainController {
    private UserService userService;
    private TablewareService tablewareService;
    private User loggedInUser;
    private MainView mainView;

    public MainController(UserService userService, TablewareService tablewareService) {
        this.userService = userService;
        this.tablewareService = tablewareService;
    }

    public void run() throws DaoException {
        Scanner sc = new Scanner(System.in);

        while (loggedInUser == null) {
            System.out.println("Please log in:");
            System.out.println("Enter your username:");
            String username = sc.nextLine();
            System.out.println("Now enter your password:");
            String password = sc.nextLine();

            User user = userService.authenticate(username, password);
            if (user != null) {
                loggedInUser = user;
            } else {
                System.out.println("Invalid username or password. Try again.");
            }
        }

        if (loggedInUser.getRole() == Role.ADMIN) {
            mainView.adminMenu();
        } else {
            mainView.userMenu();
        }
    }
}
