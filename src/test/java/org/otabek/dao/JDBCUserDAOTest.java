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
    private static final String TEST_DB_URL = "jdbc:postgresql://localhost:5432/tableware_warehouse"; // In-memory database
    private static final String TEST_DB_USER = "postgres";
    private static final String TEST_DB_PASSWORD = "1234";

    @BeforeAll
    void setupDatabase() throws Exception {
        // Create the test database and initialize schema
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
        // Truncate the table before each test to ensure a clean state
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute("TRUNCATE TABLE users");
        }
    }

    @Test
    void testCreateUser() throws DaoException {
        // Arrange
        User user = new User(0, "testuser", "password123", Role.USER);

        // Act
        User createdUser = userDao.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertTrue(createdUser.getId() > 0);
        assertEquals("testuser", createdUser.getUsername());
    }

    @Test
    void testFindUserById() throws DaoException {
        // Arrange
        User user = new User(0, "testuser", "password123", Role.USER);
        User createdUser = userDao.createUser(user);

        // Act
        User foundUser = userDao.findUserById(createdUser.getId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testFindUserByUsername() throws DaoException {
        // Arrange
        User user = new User(0, "uniqueuser", "password123", Role.USER);
        userDao.createUser(user);

        // Act
        User foundUser = userDao.findUserByUsername("uniqueuser");

        // Assert
        assertNotNull(foundUser);
        assertEquals("uniqueuser", foundUser.getUsername());
    }

    @Test
    void testUpdateUser() throws DaoException {
        // Arrange
        User user = new User(0, "olduser", "password123", Role.USER);
        User createdUser = userDao.createUser(user);

        // Act
        createdUser.setUsername("newuser");
        createdUser.setPassword("newpassword");
        createdUser.setRole(Role.ADMIN);
        User updatedUser = userDao.updateUser(createdUser);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("newuser", updatedUser.getUsername());
        assertEquals("newpassword", updatedUser.getPassword());
        assertEquals(Role.ADMIN, updatedUser.getRole());
    }

    @Test
    void testFindAllUsers() throws DaoException {
        // Arrange
        userDao.createUser(new User(0, "user1", "pass1", Role.USER));
        userDao.createUser(new User(0, "user2", "pass2", Role.ADMIN));

        // Act
        List<User> users = userDao.findAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void testDeleteUser() throws DaoException {
        // Arrange
        User user = new User(0, "todelete", "password123", Role.USER);
        User createdUser = userDao.createUser(user);

        // Act
        boolean deleted = userDao.deleteUser(createdUser.getId());

        // Assert
        assertTrue(deleted);

        // Verify the user no longer exists
        User foundUser = userDao.findUserById(createdUser.getId());
        assertNull(foundUser);
    }

    @Test
    void testDeleteNonExistentUser() throws DaoException {
        // Act
        boolean deleted = userDao.deleteUser(999); // Non-existent ID

        // Assert
        assertFalse(deleted);
    }
}
