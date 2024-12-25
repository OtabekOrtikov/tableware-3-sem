package org.otabek.dao;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;
import org.otabek.exceptions.DaoException;
import transaction.TransactionManager;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCTablewareDAO2Test {

    private JDBCTablewareDAO tablewareDAO;
    private TransactionManager transactionManager;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeAll
    void setup() {
        // Mock the TransactionManager and database connections
        transactionManager = mock(TransactionManager.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Mock the transaction manager to return mocked connection
        try {
            when(transactionManager.getConnection()).thenReturn(mockConnection);
        } catch (DaoException e) {
            fail("Failed to mock connection");
        }

        // Create the DAO with mocked TransactionManager
        tablewareDAO = new JDBCTablewareDAO(transactionManager);
    }

    @BeforeEach
    void setupMocks() throws SQLException {
        // Reset mocks before each test
        reset(mockConnection, mockPreparedStatement, mockResultSet);

        // Mock behavior for the PreparedStatement and ResultSet
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void testCreateItem() throws DaoException, SQLException {
        // Create a mock Cup item
        Tableware cup = new Cup(null, "Coffee Cup", 5.0f, "White", 10f, "Mug");

        // Mock the ResultSet to simulate a returned ID after insertion
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        // Call the method to test
        Tableware createdItem = tablewareDAO.createItem(cup);

        // Verify interactions with the mocks
        verify(mockPreparedStatement).setString(1, "Coffee Cup");
        verify(mockPreparedStatement).setFloat(2, 5.0f);
        verify(mockPreparedStatement).setString(3, "White");
        verify(mockPreparedStatement).setFloat(4, 10f);

        // Verify the returned item
        assertNotNull(createdItem);
        assertEquals(1, createdItem.getId());
        assertEquals("Coffee Cup", createdItem.getName());
    }

    @Test
    void testFindByID() throws DaoException, SQLException {
        // Create a mock Plate item
        Tableware plate = new Plate(1, "Dinner Plate", 10.0f, "Blue", 15f, 20.0f);

        // Mock the ResultSet to return the mock Plate data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Dinner Plate");
        when(mockResultSet.getFloat("radius")).thenReturn(20.0f);

        // Call the method to test
        Tableware foundItem = tablewareDAO.findByID(1);

        // Verify the returned item
        assertNotNull(foundItem);
        assertEquals("Dinner Plate", foundItem.getName());
        assertEquals(20.0f, ((Plate) foundItem).getRadius());
    }

    @Test
    void testFindAll() throws DaoException, SQLException {
        // Mock two items: Cup and Plate
        Tableware cup = new Cup(1, "Cup1", 5f, "Red", 10f, "Mug");
        Tableware plate = new Plate(2, "Plate1", 10f, "Blue", 15f, 20f);

        // Mock the ResultSet to return the two items
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("name")).thenReturn("Cup1").thenReturn("Plate1");

        // Call the method to test
        List<Tableware> items = tablewareDAO.findAll();

        // Verify the returned list
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Cup1", items.get(0).getName());
        assertEquals("Plate1", items.get(1).getName());
    }

    @Test
    void testUpdateItem() throws DaoException, SQLException {
        // Create a mock Cup item
        Tableware cup = new Cup(1, "Old Cup", 5.0f, "White", 10f, "Mug");

        // Mock the PreparedStatement behavior
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Call the method to test
        Tableware updatedItem = tablewareDAO.updateItem("name", "Updated Cup", 1);

        // Verify that the update query was executed
        verify(mockPreparedStatement).setString(1, "Updated Cup");
        verify(mockPreparedStatement).setInt(2, 1);

        // Check that the updated item has the new name
        assertNotNull(updatedItem);
        assertEquals("Updated Cup", updatedItem.getName());
    }

    @Test
    void testDeleteItem() throws DaoException, SQLException {
        // Create a mock Teapot item
        Tableware teapot = new Teapot(1, "Teapot", 8.0f, "Green", 20f, "Vintage");

        // Mock the PreparedStatement behavior
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Call the method to test
        boolean deleted = tablewareDAO.delete(1);

        // Verify that the delete query was executed
        verify(mockPreparedStatement).setInt(1, 1);

        // Assert the item was deleted
        assertTrue(deleted);
    }

    @Test
    void testDeleteNonExistentItem() throws DaoException, SQLException {
        // Mock the PreparedStatement behavior to return 0 (no rows deleted)
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        // Call the method to test
        boolean deleted = tablewareDAO.delete(999); // Non-existent ID

        // Assert the item was not deleted
        assertFalse(deleted);
    }

    @AfterAll
    void tearDown() throws SQLException {
        // Close mocks (if necessary)
        mockConnection.close();
    }
}
