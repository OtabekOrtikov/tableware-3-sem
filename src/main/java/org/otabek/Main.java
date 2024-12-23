package org.otabek;

import org.otabek.AppConfig;
import org.otabek.controller.AdminController;
import org.otabek.controller.ItemController;
import org.otabek.controller.MainController;
import org.otabek.controller.commands.CommandFactory;
import org.otabek.exceptions.DaoException;
import org.otabek.view.MainView;

public class Main {
    public static void main(String[] args) throws DaoException {
        AppConfig config = new AppConfig();
        CommandFactory commandFactory = new CommandFactory();
        AdminController adminController = new AdminController(commandFactory);
        ItemController itemController = new ItemController(commandFactory);
        MainController mainController = new MainController(adminController, itemController, commandFactory);
        MainView mainView = new MainView(mainController);

        mainView.start();
    }
}
