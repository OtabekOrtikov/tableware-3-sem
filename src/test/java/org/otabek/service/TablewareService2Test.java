package org.otabek.service;

import org.junit.jupiter.api.*;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;
import org.otabek.exceptions.DaoException;
import org.otabek.pool.ConnectionPool;
import transaction.TransactionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TablewareService2Test {

    private TablewareService tablewareService;
    private JDBCTablewareDAO tablewareDAO;
    private TransactionManager transactionManager;
    private ConnectionPool connectionPool;

    @BeforeAll
    void setup() throws Exception {
        // Initialize the ConnectionPool to point to tableware_warehousetest DB
        connectionPool = ConnectionPool.create("jdbc:postgresql://localhost:5432/tableware_warehousetest", "postgres", "1234", 10);

        // Initialize the TransactionManager with the ConnectionPool
        transactionManager = new TransactionManager(connectionPool);

        // Initialize the DAO with the TransactionManager
        tablewareDAO = new JDBCTablewareDAO(transactionManager);

        // Initialize the TablewareService with the DAO
        tablewareService = new TablewareService(tablewareDAO);
    }

    @BeforeEach
    void cleanup() throws Exception {
        // Truncate table to reset state before each test
        try (var conn = transactionManager.getConnection();
             var st = conn.createStatement()) {
            st.execute("TRUNCATE TABLE tableware RESTART IDENTITY");
        }
    }

    @Test
    void testCreateTableware() throws DaoException {
        Tableware cup = new Cup(null, "Coffee Cup", 5.0f, "White", 10f, "Mug");

        // Call the service method
        Tableware createdTableware = tablewareService.createTableware(cup);

        // Assert that the tableware is created with the correct data
        assertNotNull(createdTableware.getId(), "Created tableware should have an ID");
        assertEquals("Coffee Cup", createdTableware.getName(), "Tableware name mismatch");
    }

    @Test
    void testGetByID() throws DaoException {
        Tableware plate = new Plate(null, "Dinner Plate", 10.0f, "Blue", 15f, 20f);

        // Create a plate using the service
        Tableware createdPlate = tablewareService.createTableware(plate);

        // Retrieve the tableware by ID
        Tableware foundPlate = tablewareService.getByID(createdPlate.getId());

        // Assert that the tableware is found and has the correct data
        assertNotNull(foundPlate, "Tableware should be found by ID");
        assertEquals("Dinner Plate", foundPlate.getName(), "Tableware name mismatch");
        assertEquals(20f, ((Plate) foundPlate).getRadius(), "Radius mismatch");
    }

    @Test
    void testGetAll() throws DaoException {
        tablewareService.createTableware(new Cup(null, "Cup1", 5f, "Red", 10f, "Mug"));
        tablewareService.createTableware(new Plate(null, "Plate1", 10f, "Blue", 15f, 20f));

        // Retrieve all tableware
        List<Tableware> tablewareList = tablewareService.getAll();

        // Assert that all items are returned correctly
        assertNotNull(tablewareList, "Tableware list should not be null");
        assertEquals(2, tablewareList.size(), "Tableware list size mismatch");
        assertEquals("Cup1", tablewareList.get(0).getName(), "First tableware name mismatch");
        assertEquals("Plate1", tablewareList.get(1).getName(), "Second tableware name mismatch");
    }

    @Test
    void testUpdateTableware() throws DaoException {
        Tableware cup = new Cup(null, "Old Cup", 5.0f, "White", 10f, "Mug");

        // Create a new cup
        Tableware createdCup = tablewareService.createTableware(cup);

        // Update the cup name using the service
        Tableware updatedCup = tablewareService.updateTableware("name", "Updated Cup", createdCup.getId());

        // Assert that the cup was updated
        assertNotNull(updatedCup, "Updated tableware should not be null");
        assertEquals("Updated Cup", updatedCup.getName(), "Tableware name mismatch after update");
    }

    @Test
    void testDeleteTableware() throws DaoException {
        Tableware teapot = new Teapot(null, "Teapot", 8.0f, "Green", 20f, "Vintage");

        // Create a new teapot
        Tableware createdTeapot = tablewareService.createTableware(teapot);

        // Delete the teapot using the service
        boolean isDeleted = tablewareService.deleteTableware(createdTeapot.getId());

        // Assert that the teapot was deleted
        assertTrue(isDeleted, "Tableware should be deleted");

        // Attempt to find the deleted item
        Tableware foundTeapot = tablewareService.getByID(createdTeapot.getId());
        assertNull(foundTeapot, "Deleted tableware should not be found");
    }

    @Test
    void testDeleteNonExistentTableware() throws DaoException {
        // Attempt to delete a non-existent item
        boolean isDeleted = tablewareService.deleteTableware(999); // Non-existent ID

        // Assert that the deletion attempt returns false
        assertFalse(isDeleted, "Deleting a non-existent tableware should return false");
    }

    @AfterAll
    void tearDown() throws Exception {
        // Close the connection pool
        connectionPool.close();
    }
}
