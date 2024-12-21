package org.otabek.dao.jdbc;

import org.otabek.dao.IUserDAO;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.ConnectionPoolException;
import org.otabek.exceptions.DaoException;
import org.otabek.pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUserDAO implements IUserDAO {
    private final ConnectionPool connectionPool;

    public JDBCUserDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public User createUser(User user) throws DaoException {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?) RETURNING id";
        try (Connection conn = connectionPool.takeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                return user;
            } else {
                throw new DaoException("Error creating user");
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Error creating user", e);
        }
    }

    @Override
    public User findUserById(int id) throws DaoException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = connectionPool.takeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), Role.valueOf(rs.getString("role")));
            } else {
                return null;
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Error finding user by id", e);
        }
    }

    @Override
    public User findUserByUsername(String username) throws DaoException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = connectionPool.takeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), Role.valueOf(rs.getString("role")));
            } else {
                return null;
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Error finding user by id", e);
        }
    }

    @Override
    public User updateUser(User user) throws DaoException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = connectionPool.takeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());
            ps.setInt(4, user.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DaoException("Error updating user");
            }
            return user;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Error updating user", e);
        }
    }

    @Override
    public List<User> findAllUsers() throws DaoException {
        String sql = "SELECT * FROM users";
        try (Connection conn = connectionPool.takeConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<User> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), Role.valueOf(rs.getString("role"))));
            }
            return list;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Error finding all users", e);
        }
    }

    @Override
    public boolean deleteUser(int id) throws DaoException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = connectionPool.takeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Error deleting user", e);
        }
    }

    public void close() throws ConnectionPoolException {
        connectionPool.close();
    }
}
