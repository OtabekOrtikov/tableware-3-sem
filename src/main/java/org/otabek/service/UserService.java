package org.otabek.service;

import org.otabek.dao.IUserDAO;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;

import java.util.List;

public class UserService {
    private IUserDAO IUserDAO;

    public UserService(IUserDAO IUserDAO) {
        this.IUserDAO = IUserDAO;
    }

    public User createUser(String username, String password, Role role) throws DaoException {
        User existing = IUserDAO.findUserByUsername(username);
        if (existing != null) {
            System.out.println("User already exists!");
            return null;
        }
        User user = new User(0, username, password, role);
        return IUserDAO.createUser(user);
    }

    public User authenticate(String username, String password) throws DaoException {
        User user = IUserDAO.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User findUserByUsername(String username) throws DaoException {
        return IUserDAO.findUserByUsername(username);
    }

    public boolean deleteUser(int id) throws DaoException {
        return IUserDAO.deleteUser(id);
    }

    public List<User> listAllUsers() throws DaoException {
        return IUserDAO.findAllUsers();
    }
}
