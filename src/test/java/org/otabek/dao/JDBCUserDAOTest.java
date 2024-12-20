package org.otabek.dao;

import org.junit.jupiter.api.*;
import org.otabek.dao.jdbc.JDBCUserDAO;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCUserDAOTest {

    private JDBCUserDAO userDao;
    private static final String TEST_DB_URL = "jdbc:postgresql://localhost:5432/tableware_warehousetest";
    private static final String TEST_DB_USER = "postgres";
    private static final String TEST_DB_PASSWORD = "1234";

    @BeforeAll
    void setupDatabase() throws Exception {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(255) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(50) NOT NULL)");
        }

        userDao = new JDBCUserDAO(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
    }

    @BeforeEach
    void cleanup() throws Exception {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute("TRUNCATE TABLE users");
        }
    }

    @Test
    void testCreateUser() throws DaoException {
        User user = new User(0, "testuser", "password123", Role.USER);
        User createdUser = userDao.createUser(user);

        assertNotNull(createdUser);
        assertTrue(createdUser.getId() > 0);
        assertEquals("testuser", createdUser.getUsername());
    }

    @Test
    void testCreateDuplicateUser() {
        User user1 = new User(0, "duplicateuser", "password123", Role.USER);
        User user2 = new User(0, "duplicateuser", "password456", Role.ADMIN);

        assertDoesNotThrow(() -> userDao.createUser(user1));

        DaoException exception = assertThrows(DaoException.class, () -> userDao.createUser(user2));
        assertTrue(exception.getMessage().contains("Error creating user"));
    }

    @Test
    void testFindUserById() throws DaoException {
        User user = new User(0, "testuser", "password123", Role.USER);
        User createdUser = userDao.createUser(user);

        User foundUser = userDao.findUserById(createdUser.getId());

        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testFindUserByNonExistentId() throws DaoException {
        User foundUser = userDao.findUserById(999);
        assertNull(foundUser);
    }

    @Test
    void testFindUserByUsername() throws DaoException {
        User user = new User(0, "uniqueuser", "password123", Role.USER);
        userDao.createUser(user);

        User foundUser = userDao.findUserByUsername("uniqueuser");

        assertNotNull(foundUser);
        assertEquals("uniqueuser", foundUser.getUsername());
    }

    @Test
    void testFindUserByNonExistentUsername() throws DaoException {
        User foundUser = userDao.findUserByUsername("nonexistentuser");
        assertNull(foundUser);
    }

    @Test
    void testUpdateUser() throws DaoException {
        User user = new User(0, "olduser", "password123", Role.USER);
        User createdUser = userDao.createUser(user);

        createdUser.setUsername("newuser");
        createdUser.setPassword("newpassword");
        createdUser.setRole(Role.ADMIN);
        User updatedUser = userDao.updateUser(createdUser);

        assertNotNull(updatedUser);
        assertEquals("newuser", updatedUser.getUsername());
        assertEquals("newpassword", updatedUser.getPassword());
        assertEquals(Role.ADMIN, updatedUser.getRole());
    }

    @Test
    void testUpdateNonExistentUser() {
        User nonExistentUser = new User(999, "nonexistent", "password123", Role.USER);

        DaoException exception = assertThrows(DaoException.class, () -> userDao.updateUser(nonExistentUser));
        assertTrue(exception.getMessage().contains("Error updating user"));
    }

    @Test
    void testFindAllUsers() throws DaoException {
        userDao.createUser(new User(0, "user1", "pass1", Role.USER));
        userDao.createUser(new User(0, "user2", "pass2", Role.ADMIN));

        List<User> users = userDao.findAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void testFindAllUsersWhenEmpty() throws DaoException {
        List<User> users = userDao.findAllUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void testDeleteUser() throws DaoException {
        User user = new User(0, "todelete", "password123", Role.USER);
        User createdUser = userDao.createUser(user);

        boolean deleted = userDao.deleteUser(createdUser.getId());

        assertTrue(deleted);

        User foundUser = userDao.findUserById(createdUser.getId());
        assertNull(foundUser);
    }

    @Test
    void testDeleteNonExistentUser() throws DaoException {
        boolean deleted = userDao.deleteUser(999);

        assertFalse(deleted);
    }
}
