package org.otabek.view;

import java.util.Scanner;

public class MainView {
    private Scanner sc = new Scanner(System.in);

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public String readLine() {
        return sc.nextLine();
    }

    public void displayLoginPrompt() {
        displayMessage("Please log in:");
    }

    public String readUsername() {
        System.out.println("Enter your username:");
        return sc.nextLine();
    }

    public String readPassword() {
        System.out.println("Now enter your password:");
        return sc.nextLine();
    }

    public void displayInvalidCredentialsMessage() {
        displayMessage("Invalid username or password. Try again.");
    }

    public void displayAdminMenu() {
        displayMessage("""
                Admin Menu:
                
                1. Manage Users - /users;
                2. Manage Items - /items;
                3. Exit - /exit;
                
                Please enter command name or number:""");
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void displayUserMenu() {
        displayMessage("""
                User Menu:
                
                1. List Items - /items;
                2. Exit - /exit;
                
                Please enter command name or number:""");
    }

    public void displayManageUsersMenu() {
        displayMessage("""
                Manage Users:
                
                1. List all users - /list;
                2. Create user - /create;
                3. Delete user - /delete;
                4. Back - /back;
            
                Please enter command name or number:""");
    }

    public void displayManageItemsMenu() {
        displayMessage("""
                Manage Items:
                
                1. List all items - /list;
                2. Create item - /create;
                3. Update item - /update;
                4. Delete item - /delete;
                5. Back - /back;
                
                Please enter command name or number:""");
    }


    public void displayCreateItemMenu() {
        System.out.println("""
                Which type of item do you want to create?
                
                1. Plate;
                2. Cup;
                3. Teapot;
                
                Please enter number or name of the item:""");
    }

    public void displayInvalidCommandMessage() {
        displayMessage("Invalid command. Try again.");
    }

    public String prompt(String message) {
        displayMessage(message);
        return readLine();
    }
}
