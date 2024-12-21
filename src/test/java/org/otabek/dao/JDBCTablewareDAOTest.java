package org.otabek.dao;

import org.junit.jupiter.api.*;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;
import org.otabek.exceptions.DaoException;
import org.otabek.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCTablewareDAOTest {

    private JDBCTablewareDAO tablewareDAO;
    private ConnectionPool connectionPool;

    @BeforeAll
    void setupDatabase() throws Exception {
        // Initialize ConnectionPool
        connectionPool = ConnectionPool.create(
                "jdbc:postgresql://localhost:5432/tableware_warehousetest",
                "postgres",
                "1234",
                10 // Pool size
        );

        // Create schema
        try (Connection conn = connectionPool.takeConnection();
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

        tablewareDAO = new JDBCTablewareDAO(connectionPool);
    }

    @BeforeEach
    void cleanup() throws Exception {
        // Truncate table to reset state
        try (Connection conn = connectionPool.takeConnection();
             Statement st = conn.createStatement()) {
            st.execute("TRUNCATE TABLE tableware RESTART IDENTITY");
        }
    }

    @Test
    void testCreateItem() throws DaoException {
        Tableware cup = new Cup(null, "Coffee Cup", 5.0f, "White", 10f, "Mug");

        Tableware createdItem = tablewareDAO.createItem(cup);

        assertNotNull(createdItem, "Created item should not be null");
        assertNotNull(createdItem.getId(), "Created item ID should not be null");
        assertEquals("Coffee Cup", createdItem.getName(), "Item name mismatch");
    }

    @Test
    void testFindByID() throws DaoException {
        Tableware plate = new Plate(null, "Dinner Plate", 10.0f, "Blue", 15f, 20.0f);
        Tableware createdItem = tablewareDAO.createItem(plate);

        Tableware foundItem = tablewareDAO.findByID(createdItem.getId());

        assertNotNull(foundItem, "Found item should not be null");
        assertEquals("Dinner Plate", foundItem.getName(), "Item name mismatch");
        assertEquals(20.0f, ((Plate) foundItem).getRadius(), "Item radius mismatch");
    }

    @Test
    void testFindAll() throws DaoException {
        tablewareDAO.createItem(new Cup(null, "Cup1", 5f, "Red", 10f, "Mug"));
        tablewareDAO.createItem(new Plate(null, "Plate1", 10f, "Blue", 15f, 20f));

        List<Tableware> items = tablewareDAO.findAll();

        assertNotNull(items, "Items list should not be null");
        assertEquals(2, items.size(), "Items list size mismatch");
        assertEquals("Cup1", items.get(0).getName(), "First item name mismatch");
        assertEquals("Plate1", items.get(1).getName(), "Second item name mismatch");
    }

    @Test
    void testUpdateItem() throws DaoException {
        Tableware cup = new Cup(null, "Old Cup", 5.0f, "White", 10f, "Mug");
        Tableware createdItem = tablewareDAO.createItem(cup);

        Tableware updatedItem = tablewareDAO.updateItem("name", "Updated Cup", createdItem.getId());

        assertNotNull(updatedItem, "Updated item should not be null");
        assertEquals("Updated Cup", updatedItem.getName(), "Updated item name mismatch");
    }

    @Test
    void testDeleteItem() throws DaoException {
        Tableware teapot = new Teapot(null, "Tea Pot", 8.0f, "Green", 20f, "Vintage");
        Tableware createdItem = tablewareDAO.createItem(teapot);

        boolean deleted = tablewareDAO.delete(createdItem.getId());

        assertTrue(deleted, "Item should be deleted");

        Tableware foundItem = tablewareDAO.findByID(createdItem.getId());
        assertNull(foundItem, "Deleted item should not be found");
    }

    @Test
    void testDeleteNonExistentItem() throws DaoException {
        boolean deleted = tablewareDAO.delete(999); // Non-existent ID

        assertFalse(deleted, "Deleting a non-existent item should return false");
    }

    @AfterAll
    void tearDown() throws Exception {
        // Drop the table and close the connection pool
        try (Connection conn = connectionPool.takeConnection();
             Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS tableware");
        }
        connectionPool.close();
    }
}
