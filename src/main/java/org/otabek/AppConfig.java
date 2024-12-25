package org.otabek;

import org.otabek.controller.MainController;
import org.otabek.dao.IUserDAO;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.dao.jdbc.JDBCUserDAO;
import org.otabek.entity.Role;
import org.otabek.exceptions.ConnectionPoolException;
import org.otabek.exceptions.DaoException;
import org.otabek.pool.ConnectionPool;
import org.otabek.service.TablewareService;
import org.otabek.service.UserService;
import org.otabek.view.MainView;
import transaction.TransactionManager;

public class AppConfig {
    private ConnectionPool connectionPool;
    private TransactionManager transactionManager;

    private JDBCTablewareDAO tablewareDAO;
    private IUserDAO userDAO;
    private TablewareService tablewareService;
    private UserService userService;
    private MainController mainController;

    public AppConfig() throws DaoException {
        init();
    }

    private void init() throws DaoException {
        try {
            // Initialize the connection pool
            String dbUrl = "jdbc:postgresql://localhost:5432/tableware_warehouse";
            String dbUsername = "postgres";
            String dbPassword = "1234";
            int poolSize = 10;
            connectionPool = ConnectionPool.create(dbUrl, dbUsername, dbPassword, poolSize);
            transactionManager = new TransactionManager(connectionPool);

            // Initialize DAOs with the connection pool
            tablewareDAO = new JDBCTablewareDAO(transactionManager);
            userDAO = new JDBCUserDAO(transactionManager);

            // Initialize services and controllers
            tablewareService = new TablewareService(tablewareDAO);
            userService = new UserService(userDAO);
            MainView view = new MainView();
            mainController = new MainController(view, tablewareService, userService);

            // Ensure the admin user exists
            if (userService.findUserByUsername("admin") == null) {
                userService.createUser("admin", "admin", Role.ADMIN);
            }
        } catch (ConnectionPoolException e) {
            throw new DaoException("Error initializing the connection pool", e);
        }
    }

    public MainController getMainController() {
        return mainController;
    }

    public void close() throws ConnectionPoolException {
        connectionPool.close();
    }
}