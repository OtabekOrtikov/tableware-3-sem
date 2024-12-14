package org.otabek.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.Tableware;
import org.otabek.exceptions.DaoException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TablewareServiceTest {

    private JDBCTablewareDAO tablewareDaoMock;
    private TablewareService tablewareService;

    @BeforeEach
    void setUp() {
        // Mock the DAO
        tablewareDaoMock = Mockito.mock(JDBCTablewareDAO.class);
        // Initialize the service with the mocked DAO
        tablewareService = new TablewareService(tablewareDaoMock);
    }

    @Test
    void testCreateTableware() throws DaoException {
        // Arrange: Mock DAO behavior
        Tableware cup = new Cup(0, "Coffee Cup", 5.0f, "White", 10f, "Mug");
        when(tablewareDaoMock.createItem(any(Tableware.class))).thenAnswer(invocation -> {
            Tableware t = invocation.getArgument(0);
            t.setId(1); // Simulate database assigning an ID
            return t;
        });

        // Act: Call the service method
        Tableware createdTableware = tablewareService.createTableware(cup);

        // Assert: Verify results
        assertNotNull(createdTableware);
        assertEquals(1, createdTableware.getId());
        assertEquals("Coffee Cup", createdTableware.getName());

        // Verify that the DAO's createItem was called exactly once
        verify(tablewareDaoMock, times(1)).createItem(cup);
    }

    @Test
    void testGetByID() throws DaoException {
        // Arrange: Mock DAO behavior
        Tableware plate = new Plate(1, "Dinner Plate", 10.0f, "Blue", 15f, 20.0f);
        when(tablewareDaoMock.findByID(1)).thenReturn(plate);

        // Act: Call the service method
        Tableware foundTableware = tablewareService.getByID(1);

        // Assert: Verify results
        assertNotNull(foundTableware);
        assertEquals("Dinner Plate", foundTableware.getName());
        assertEquals(15f, foundTableware.getPrice());

        // Verify that the DAO's findByID was called exactly once
        verify(tablewareDaoMock, times(1)).findByID(1);
    }

    @Test
    void testGetAll() throws DaoException {
        // Arrange: Mock DAO behavior
        List<Tableware> items = Arrays.asList(
                new Cup(1, "Cup1", 5f, "Red", 10f, "Mug"),
                new Plate(2, "Plate1", 10f, "Blue", 15f, 20f)
        );
        when(tablewareDaoMock.findAll()).thenReturn(items);

        // Act: Call the service method
        List<Tableware> tablewareList = tablewareService.getAll();

        // Assert: Verify results
        assertNotNull(tablewareList);
        assertEquals(2, tablewareList.size());
        assertEquals("Cup1", tablewareList.get(0).getName());
        assertEquals("Plate1", tablewareList.get(1).getName());

        // Verify that the DAO's findAll was called exactly once
        verify(tablewareDaoMock, times(1)).findAll();
    }

    @Test
    void testUpdateTableware() throws DaoException {
        // Arrange: Mock DAO behavior
        Tableware cup = new Cup(1, "Updated Cup", 5.0f, "White", 12f, "Mug");
        when(tablewareDaoMock.updateItem("price", "12.0", 1)).thenReturn(cup);

        // Act: Call the service method
        Tableware updatedTableware = tablewareService.updateTableware("price", "12.0", 1);

        // Assert: Verify results
        assertNotNull(updatedTableware);
        assertEquals("Updated Cup", updatedTableware.getName());
        assertEquals(12f, updatedTableware.getPrice());

        // Verify that the DAO's updateItem was called exactly once
        verify(tablewareDaoMock, times(1)).updateItem("price", "12.0", 1);
    }

    @Test
    void testDeleteTableware() throws DaoException {
        // Arrange: Mock DAO behavior
        when(tablewareDaoMock.delete(1)).thenReturn(true);

        // Act: Call the service method
        boolean deleted = tablewareService.deleteTableware(1);

        // Assert: Verify results
        assertTrue(deleted);

        // Verify that the DAO's delete method was called exactly once
        verify(tablewareDaoMock, times(1)).delete(1);
    }

    @Test
    void testDeleteTablewareNotFound() throws DaoException {
        // Arrange: Mock DAO behavior
        when(tablewareDaoMock.delete(1)).thenReturn(false);

        // Act: Call the service method
        boolean deleted = tablewareService.deleteTableware(1);

        // Assert: Verify results
        assertFalse(deleted);

        // Verify that the DAO's delete method was called exactly once
        verify(tablewareDaoMock, times(1)).delete(1);
    }
}
