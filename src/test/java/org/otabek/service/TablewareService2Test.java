package org.otabek.service;

import org.junit.jupiter.api.*;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.exceptions.DaoException;
import org.otabek.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TablewareService2Test {

    private ConnectionPool connectionPool;
    private JDBCTablewareDAO dao;
    private TablewareService service;

    @BeforeAll
    void setUpDatabase() throws Exception {
        // Initialize ConnectionPool
        connectionPool = ConnectionPool.create(
                "jdbc:postgresql://localhost:5432/tableware_warehousetest",
                "postgres",
                "1234",
                10 // Pool size
        );

        // Initialize DAO and Service
        dao = new JDBCTablewareDAO(connectionPool);
        service = new TablewareService(dao);

        // Set up the table
        try (Connection connection = connectionPool.takeConnection();
             Statement stmt = connection.createStatement()) {
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
        try (Connection connection = connectionPool.takeConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE tableware RESTART IDENTITY");
        }
    }

    @Test
    void testCreateAndRetrieveTableware() throws DaoException {
        Tableware cup = new Cup(null, "Small Cup", 10.0f, "Red", 5.99f, "Tea");
        Tableware created = service.createTableware(cup);

        assertNotNull(created.getId(), "Created tableware ID should not be null");
        assertEquals("Small Cup", created.getName(), "Tableware name mismatch");

        Tableware retrieved = service.getByID(created.getId());
        assertNotNull(retrieved, "Retrieved tableware should not be null");
        assertEquals("Small Cup", retrieved.getName(), "Retrieved tableware name mismatch");
    }

    @Test
    void testGetAllTableware() throws DaoException {
        Tableware cup1 = new Cup(null, "Cup A", 10.0f, "Red", 5.99f, "Tea");
        Tableware cup2 = new Cup(null, "Cup B", 12.0f, "Blue", 6.99f, "Coffee");

        service.createTableware(cup1);
        service.createTableware(cup2);

        List<Tableware> allItems = service.getAll();
        assertEquals(2, allItems.size(), "There should be 2 items in the tableware list");
    }

    @Test
    void testUpdateTableware() throws DaoException {
        // Step 1: Create a tableware item
        Tableware cup = new Cup(null, "Old Cup", 10.0f, "Red", 5.99f, "Tea");
        Tableware created = service.createTableware(cup);

        // Step 2: Update the tableware's name
        String newName = "Updated Cup";
        Tableware updated = service.updateTableware("name", newName, created.getId());

        // Step 3: Assert the results
        assertNotNull(updated, "Updated tableware should not be null");
        assertEquals(newName, updated.getName(), "Updated tableware name mismatch");
    }


    @Test
    void testDeleteTableware() throws DaoException {
        Tableware cup = new Cup(null, "Cup to Delete", 10.0f, "Red", 5.99f, "Tea");
        Tableware created = service.createTableware(cup);

        boolean deleted = service.deleteTableware(created.getId());
        assertTrue(deleted, "Tableware should be deleted");

        Tableware retrieved = service.getByID(created.getId());
        assertNull(retrieved, "Deleted tableware should not be found");
    }

    @AfterAll
    void tearDown() throws Exception {
        // Drop the table and close the connection pool
        try (Connection connection = connectionPool.takeConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS tableware");
        }
        connectionPool.close();
    }
}
