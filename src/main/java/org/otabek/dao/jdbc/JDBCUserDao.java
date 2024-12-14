package org.otabek.dao.jdbc;

import org.otabek.dao.UserDAO;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUserDao implements UserDAO {
    private String url;
    private String user;
    private String password;

    public JDBCUserDao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public User createUser(User user) throws DaoException {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?) RETURNING id";
        try (Connection conn = DriverManager.getConnection(url, this.user, this.password); PreparedStatement ps = conn.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            throw new DaoException("Error creating user", e);
        }
    }

    @Override
    public User findUserById(int id) throws DaoException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), Role.valueOf(rs.getString("role")));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding user by id", e);
        }
    }

    @Override
    public User findUserByUsername(String username) throws DaoException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), Role.valueOf(rs.getString("role").toUpperCase()));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding user by username", e);
        }
    }

    @Override
    public User updateUser(User user) throws DaoException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, this.user, password); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());
            ps.setInt(4, user.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DaoException("Error updating user");
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException("Error updating user", e);
        }
    }

    @Override
    public List<User> findAllUsers() throws DaoException {
        String sql = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(url, user, password); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            List<User> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), Role.valueOf(rs.getString("role").toUpperCase())));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException("Error finding all users", e);
        }
    }

    @Override
    public boolean deleteUser(int id) throws DaoException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DaoException("Error deleting user", e);
        }
    }
}
