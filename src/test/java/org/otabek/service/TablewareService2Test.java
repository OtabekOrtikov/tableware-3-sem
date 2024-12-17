package org.otabek.service;

import org.junit.jupiter.api.*;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TablewareService2Test {

    private static Connection connection;
    private static JDBCTablewareDAO dao;
    private static TablewareService service;

    @BeforeAll
    static void setUpDatabase() throws Exception {
        // Connect to the PostgreSQL or H2 database
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tableware_warehousetest", "postgres", "1234");

        // Initialize DAO and Service
        dao = new JDBCTablewareDAO("jdbc:postgresql://localhost:5432/tableware_warehousetest", "postgres", "1234");
        service = new TablewareService(dao);

        // Set up the table
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tableware (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "width FLOAT, " +
                    "color VARCHAR(255), " +
                    "price FLOAT, " +
                    "type VARCHAR(50), " +
                    "category VARCHAR(255), " +
                    "radius FLOAT, " +
                    "style VARCHAR(255)" +
                    ")");
        }
    }

    @BeforeEach
    void cleanTable() throws Exception {
        // Clear the table before each test
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM tableware");
        }
    }

    @Test
    void testCreateAndRetrieveTableware() throws DaoException {
        Tableware cup = new Cup(1, "Small Cup", 10.0f, "Red", 5.99f, "Tea");
        Tableware created = service.createTableware(cup);

        assertNotNull(created.getId());
        assertEquals("Small Cup", created.getName());

        Tableware retrieved = service.getByID(created.getId());
        assertNotNull(retrieved);
        assertEquals("Small Cup", retrieved.getName());
    }

    @Test
    void testGetAllTableware() throws DaoException {
        Tableware cup1 = new Cup(1, "Cup A", 10.0f, "Red", 5.99f, "Tea");
        Tableware cup2 = new Cup(2, "Cup B", 12.0f, "Blue", 6.99f, "Coffee");

        service.createTableware(cup1);
        service.createTableware(cup2);

        List<Tableware> allItems = service.getAll();
        assertEquals(2, allItems.size());
    }

    @Test
    void testUpdateTableware() throws DaoException {
        Tableware cup = new Cup(1, "Old Cup", 10.0f, "Red", 5.99f, "Tea");
        Tableware created = service.createTableware(cup);

        Tableware updated = service.updateTableware("name", "Updated Cup", created.getId());
        assertNotNull(updated);
        assertEquals("Updated Cup", updated.getName());
    }

    @Test
    void testDeleteTableware() throws DaoException {
        Tableware cup = new Cup(1, "Cup to Delete", 10.0f, "Red", 5.99f, "Tea");
        Tableware created = service.createTableware(cup);

        boolean deleted = service.deleteTableware(created.getId());
        assertTrue(deleted);

        Tableware retrieved = service.getByID(created.getId());
        assertNull(retrieved);
    }

    @AfterAll
    static void tearDown() throws Exception {
        // Drop the table and close the connection
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS tableware");
        }
        connection.close();
    }
}
