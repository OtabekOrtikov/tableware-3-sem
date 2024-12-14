package org.otabek.service;

import org.otabek.dao.UserDAO;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;

import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(String username, String password, Role role) throws DaoException {
        User existing = userDAO.findUserByUsername(username);
        if (existing != null) {
            System.out.println("User already exists!");
            return null;
        }
        User user = new User(0, username, password, role);
        return userDAO.createUser(user);
    }

    public User authenticate(String username, String password) throws DaoException {
        User user = userDAO.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User findUserByUsername(String username) throws DaoException {
        return userDAO.findUserByUsername(username);
    }

    public boolean deleteUser(int id) throws DaoException {
        return userDAO.deleteUser(id);
    }

    public List<User> listAllUsers() throws DaoException {
        return userDAO.findAllUsers();
    }
}
