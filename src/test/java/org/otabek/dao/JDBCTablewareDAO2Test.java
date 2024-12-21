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
public class JDBCTablewareDAO2Test {

    private ConnectionPool connectionPool;
    private JDBCTablewareDAO dao;

    @BeforeAll
    void setUpDatabase() throws Exception {
        connectionPool = ConnectionPool.create("jdbc:postgresql://localhost:5432/tableware_warehousetest",
                "postgres", "1234", 10);
        dao = new JDBCTablewareDAO(connectionPool);

        try (Connection connection = connectionPool.takeConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tableware (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255), width FLOAT, color VARCHAR(255), " +
                    "price FLOAT, type VARCHAR(50), category VARCHAR(255), " +
                    "radius FLOAT, style VARCHAR(255)" +
                    ")");
            stmt.execute("TRUNCATE TABLE tableware RESTART IDENTITY");
        }
    }

    @Test
    void testCreateAndFindCup() throws DaoException {
        Tableware cup = new Cup(1, "Small Cup", 11.0f, "Blue", 15.99f, "Coffee");
        Tableware createdCup = dao.createItem(cup);

        assertNotNull(createdCup.getId(), "Created cup ID should not be null");
        assertEquals("Small Cup", createdCup.getName(), "Cup name mismatch");

        Tableware foundItem = dao.findByID(createdCup.getId());
        assertNotNull(foundItem, "Found item should not be null");
        assertEquals("Small Cup", foundItem.getName(), "Found item name mismatch");
        assertTrue(foundItem instanceof Cup, "Found item should be a Cup");
    }

    @Test
    void testCreateAndFindPlate() throws DaoException {
        Tableware plate = new Plate(1, "Dinner Plate", 15.0f, "White", 25.99f, 30.0f);
        Tableware createdPlate = dao.createItem(plate);

        assertNotNull(createdPlate.getId(), "Created plate ID should not be null");
        assertEquals("Dinner Plate", createdPlate.getName(), "Plate name mismatch");

        Tableware foundItem = dao.findByID(createdPlate.getId());
        assertNotNull(foundItem, "Found item should not be null");
        assertEquals("Dinner Plate", foundItem.getName(), "Found item name mismatch");
        assertTrue(foundItem instanceof Plate, "Found item should be a Plate");
    }

    @Test
    void testCreateAndFindTeapot() throws DaoException {
        Tableware teapot = new Teapot(1, "Ceramic Teapot", 20.0f, "Red", 45.50f, "Vintage");
        Tableware createdTeapot = dao.createItem(teapot);

        assertNotNull(createdTeapot.getId(), "Created teapot ID should not be null");
        assertEquals("Ceramic Teapot", createdTeapot.getName(), "Teapot name mismatch");

        Tableware foundItem = dao.findByID(createdTeapot.getId());
        assertNotNull(foundItem, "Found item should not be null");
        assertEquals("Ceramic Teapot", foundItem.getName(), "Found item name mismatch");
        assertTrue(foundItem instanceof Teapot, "Found item should be a Teapot");
    }

    @Test
    void testFindAllItems() throws DaoException {
        dao.createItem(new Cup(null, "Cup A", 10.0f, "Green", 12.50f, "Tea"));
        dao.createItem(new Plate(null, "Plate A", 18.0f, "Blue", 22.99f, 25.0f));
        dao.createItem(new Teapot(null, "Teapot A", 25.0f, "Black", 39.99f, "Modern"));

        List<Tableware> allItems = dao.findAll();
        assertEquals(3, allItems.size(), "Should find exactly 3 items");
    }

    @Test
    void testDeleteItem() throws DaoException {
        Tableware cup = new Cup(null, "To Be Deleted", 11.0f, "Blue", 15.99f, "Coffee");
        Tableware createdCup = dao.createItem(cup);

        boolean isDeleted = dao.delete(createdCup.getId());
        assertTrue(isDeleted, "Item should be deleted");

        Tableware found = dao.findByID(createdCup.getId());
        assertNull(found, "Deleted item should not be found");
    }

    @AfterAll
    void tearDown() throws Exception {
        try (Connection connection = connectionPool.takeConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS tableware");
        }
        connectionPool.close();
    }
}
