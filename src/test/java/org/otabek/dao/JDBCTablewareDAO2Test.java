package org.otabek.dao;

import org.junit.jupiter.api.*;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCTablewareDAO2Test {

    private static Connection connection;
    private static JDBCTablewareDAO dao;

    @BeforeAll
    static void setUpDatabase() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tableware_warehousetest", "postgres", "1234");
        dao = new JDBCTablewareDAO("jdbc:postgresql://localhost:5432/tableware_warehousetest", "postgres", "1234");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tableware (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255), width FLOAT, color VARCHAR(255), " +
                    "price FLOAT, type VARCHAR(50), category VARCHAR(255), " +
                    "radius FLOAT, style VARCHAR(255)" +
                    ")");
        }
    }

    @Test
    void testCreateAndFindItem() throws DaoException {
        Tableware cup = new Cup(1, "Small Cup", 11.0f, "Blue", 15.99f, "Coffee");
        Tableware createdCup = dao.createItem(cup);

        assertNotNull(createdCup.getId());

        Tableware foundItem = dao.findByID(createdCup.getId());
        assertNotNull(foundItem);
        assertEquals("Small Cup", foundItem.getName());
    }

    @Test
    void testDeleteItem() throws DaoException {
        Tableware cup = new Cup(1, "To Be Deleted", 11.0f, "Blue", 15.99f, "Coffee");
        Tableware createdCup = dao.createItem(cup);

        boolean isDeleted = dao.delete(createdCup.getId());
        assertTrue(isDeleted);

        Tableware found = dao.findByID(createdCup.getId());
        assertNull(found);
    }

    @AfterAll
    static void tearDown() throws Exception {
        connection.close();
    }
}
