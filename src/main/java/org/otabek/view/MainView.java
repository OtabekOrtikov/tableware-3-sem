package org.otabek.view;

import org.otabek.entity.Role;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;

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
        System.out.println("""
                \nManage Users:
                1. List users
                2. Create user
                3. Delete user
                4. Change admin
                5. Back
                """);
        System.out.print("Enter command: ");
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

    public void displaySuccessMessage(String message) {
        displayMessage("Success: " + message);
    }

    public void displayErrorMessage(String message) {
        displayMessage("Error: " + message);
    }

    public void displayUser(String userInfo) {
        displayMessage(userInfo);
    }

    public void displayItem(String itemInfo) {
        displayMessage(itemInfo);
    }

    public String requestItemType() {
        displayCreateItemMenu();
        return readLine();
    }

    public Tableware requestItemDetails(String itemType) {
        String name = prompt("Enter item's name:");
        float width = Float.parseFloat(prompt("Enter item's width:"));
        String color = prompt("Enter item's color:");
        float price = Float.parseFloat(prompt("Enter item's price:"));

        switch (itemType.toLowerCase()) {
            case "plate", "1":
                float radius = Float.parseFloat(prompt("Enter plate's radius:"));
                return new Plate(0, name, width, color, price, radius);
            case "cup", "2":
                String category = prompt("Enter cup's category:");
                return new Cup(0, name, width, color, price, category);
            case "teapot", "3":
                String style = prompt("Enter teapot's style:");
                return new Teapot(0, name, width, color, price, style);
            default:
                displayMessage("Invalid item type. Please try again.");
                return null;
        }
    }

    public String requestUsernameForNewUser() {
        displayMessage("Enter username for new user:");
        return readLine();
    }

    public String requestPasswordForNewUser() {
        displayMessage("Enter password for new user:");
        return readLine();
    }

    public Role requestRoleForNewUser() {
        displayMessage("Enter role for new user (ADMIN/USER):");
        String role = readLine().toUpperCase();
        return Role.valueOf(role);
    }

    public int requestUserIdForDeletion() {
        displayMessage("Enter user ID to delete:");
        return Integer.parseInt(readLine());
    }

    public int requestItemIdForUpdate() {
        displayMessage("Enter item ID to update:");
        return Integer.parseInt(readLine());
    }

    public String requestColumnForUpdate() {
        displayMessage("Enter column to update (e.g., name, price, etc.):");
        return readLine();
    }

    public String requestNewValueForUpdate() {
        displayMessage("Enter new value for update:");
        return readLine();
    }

    public int requestItemIdForDeletion() {
        displayMessage("Enter item ID to delete:");
        return Integer.parseInt(readLine());
    }

    public int requestUserIdForRoleChange() {
        System.out.print("Enter the ID of the user to become the new admin: ");
        return Integer.parseInt(sc.nextLine());
    }
}