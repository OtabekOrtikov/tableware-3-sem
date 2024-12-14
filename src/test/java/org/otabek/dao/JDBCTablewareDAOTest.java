package org.otabek.dao;

import org.junit.jupiter.api.*;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;
import org.otabek.exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCTablewareDAOTest {

    private JDBCTablewareDAO tablewareDAO;
    private static final String TEST_DB_URL = "jdbc:postgresql://localhost:5432/tableware_warehouse"; // In-memory database
    private static final String TEST_DB_USER = "postgres";
    private static final String TEST_DB_PASSWORD = "1234";

    @BeforeAll
    void setupDatabase() throws Exception {
        // Create the test database and initialize schema
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS tableware (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        width FLOAT NOT NULL,
                        color VARCHAR(50),
                        price FLOAT NOT NULL,
                        type VARCHAR(50) NOT NULL,
                        category VARCHAR(255),
                        radius FLOAT,
                        style VARCHAR(255)
                    );
                    """);
        }

        tablewareDAO = new JDBCTablewareDAO(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
    }

    @BeforeEach
    void cleanup() throws Exception {
        // Truncate the table before each test to ensure a clean state
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute("TRUNCATE TABLE tableware");
        }
    }

    @Test
    void testCreateItem() throws DaoException {
        // Arrange
        Tableware cup = new Cup(0, "Coffee Cup", 5.0f, "White", 10f, "Mug");

        // Act
        Tableware createdItem = tablewareDAO.createItem(cup);

        // Assert
        assertNotNull(createdItem);
        assertTrue(createdItem.getId() > 0);
        assertEquals("Coffee Cup", createdItem.getName());
    }

    @Test
    void testFindByID() throws DaoException {
        // Arrange
        Tableware plate = new Plate(0, "Dinner Plate", 10.0f, "Blue", 15f, 20.0f);
        Tableware createdItem = tablewareDAO.createItem(plate);

        // Act
        Tableware foundItem = tablewareDAO.findByID(createdItem.getId());

        // Assert
        assertNotNull(foundItem);
        assertEquals("Dinner Plate", foundItem.getName());
        assertEquals(20.0f, ((Plate) foundItem).getRadius());
    }

    @Test
    void testFindAll() throws DaoException {
        // Arrange
        tablewareDAO.createItem(new Cup(0, "Cup1", 5f, "Red", 10f, "Mug"));
        tablewareDAO.createItem(new Plate(0, "Plate1", 10f, "Blue", 15f, 20f));

        // Act
        List<Tableware> items = tablewareDAO.findAll();

        // Assert
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Cup1", items.get(0).getName());
        assertEquals("Plate1", items.get(1).getName());
    }

    @Test
    void testUpdateItem() throws DaoException {
        // Arrange
        Tableware cup = new Cup(0, "Old Cup", 5.0f, "White", 10f, "Mug");
        Tableware createdItem = tablewareDAO.createItem(cup);

        // Act
        Tableware updatedItem = tablewareDAO.updateItem("name", "Updated Cup", createdItem.getId());

        // Assert
        assertNotNull(updatedItem);
        assertEquals("Updated Cup", updatedItem.getName());
    }

    @Test
    void testDeleteItem() throws DaoException {
        // Arrange
        Tableware teapot = new Teapot(0, "Tea Pot", 8.0f, "Green", 20f, "Vintage");
        Tableware createdItem = tablewareDAO.createItem(teapot);

        // Act
        boolean deleted = tablewareDAO.delete(createdItem.getId());

        // Assert
        assertTrue(deleted);

        // Verify the item no longer exists
        Tableware foundItem = tablewareDAO.findByID(createdItem.getId());
        assertNull(foundItem);
    }

    @Test
    void testDeleteNonExistentItem() throws DaoException {
        // Act
        boolean deleted = tablewareDAO.delete(999); // Non-existent ID

        // Assert
        assertFalse(deleted);
    }
}
