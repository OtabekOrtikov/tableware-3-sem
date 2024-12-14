package org.otabek.view;

import java.util.Scanner;

public class MainView {
    public void adminMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Admin Menu:
                    1. Manage Items - /items;
                    2. Manage Users - /users;
                    3. Logout - /logout;
                    
                    Please enter the command or number of the command:""");
            String command = sc.nextLine();
            switch (command) {
                case "1", "/items" -> {
                    System.out.println("Items menu");
                }
                case "2", "/users" -> {
                    System.out.println("Users menu");
                }
                case "3", "/logout" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid command. Please try again.");
            }
        }
    }

    public void userMenu() {

    }
}
