package org.otabek.dao;

import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;

import java.util.List;

public interface UserDAO {
    User createUser(User user) throws DaoException;
    User findUserById(int id) throws DaoException;
    User findUserByUsername(String username) throws DaoException;
    User updateUser(User user) throws DaoException;
    List<User> findAllUsers() throws DaoException;
    boolean deleteUser(int id) throws DaoException;
}
