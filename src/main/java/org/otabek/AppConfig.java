package org.otabek;

import org.otabek.controller.MainController;
import org.otabek.dao.IUserDAO;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.dao.jdbc.JDBCUserDAO;
import org.otabek.entity.Role;
import org.otabek.exceptions.DaoException;
import org.otabek.service.TablewareService;
import org.otabek.service.UserService;
import org.otabek.view.MainView;

public class AppConfig {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    private JDBCTablewareDAO tablewareDAO;
    private IUserDAO IUserDAO;
    private TablewareService tablewareService;
    private UserService userService;
    private MainController mainController;

    public AppConfig() throws DaoException {
        dbUrl = "jdbc:postgresql://localhost:5432/tableware_warehouse";
        dbUsername = "postgres";
        dbPassword = "1234";

        init();
    }

    private void init() throws DaoException {
        tablewareDAO = new JDBCTablewareDAO(dbUrl, dbUsername, dbPassword);
        IUserDAO = new JDBCUserDAO(dbUrl, dbUsername, dbPassword);
        tablewareService = new TablewareService(tablewareDAO);
        userService = new UserService(IUserDAO);
        MainView view = new MainView();
        mainController = new MainController(view, tablewareService, userService);

        if (userService.findUserByUsername("admin") == null) {
            userService.createUser("admin", "admin", Role.ADMIN);
        }
    }

    public MainController getMainController() {
        return mainController;
    }
}
