package org.otabek.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.entity.Tableware;
import org.otabek.exceptions.DaoException;
import org.otabek.service.UserService;
import org.otabek.service.TablewareService;
import org.otabek.view.MainView;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private TablewareService tablewareService;
    @Mock
    private MainView mainView;

    private MainController mainController;

    @BeforeAll
    void setup() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Create the MainController with mocked dependencies
        mainController = new MainController(mainView, tablewareService, userService);
    }

    @Test
    void testCreateItem() throws DaoException {
        // Prepare mock item
        Tableware mockTableware = mock(Tableware.class);

        // Mock the request for item details and tableware creation
        when(mainView.requestItemType()).thenReturn("Cup");
        when(mainView.requestItemDetails("Cup")).thenReturn(mockTableware);

        // Run the controller's createItem method
        mainController.createItem();

        // Verify that the item was created and the success message was shown
        verify(tablewareService).createTableware(mockTableware);
        verify(mainView).displaySuccessMessage("Cup created successfully.");
    }

    @Test
    void testDeleteItem() throws DaoException {
        // Prepare mock item
        Tableware mockTableware = mock(Tableware.class);

        // Mock the behavior for the item to be deleted
        when(mainView.requestItemIdForDeletion()).thenReturn(1);
        when(tablewareService.deleteTableware(1)).thenReturn(true);

        // Run the controller's deleteItem method
        mainController.deleteItem();

        // Verify that the delete method is called exactly once
        verify(tablewareService, times(1)).deleteTableware(1);

        // Verify that the success message is shown after deletion
        verify(mainView).displaySuccessMessage("Item deleted successfully.");
    }

    @Test
    void testDeleteItemFailure() throws DaoException {
        // Mock the item deletion failure
        when(mainView.requestItemIdForDeletion()).thenReturn(1);
        when(tablewareService.deleteTableware(1)).thenReturn(false);

        // Run the controller's deleteItem method
        mainController.deleteItem();

        // Verify that the error message is shown for item not found
        verify(mainView).displayErrorMessage("Item not found.");
    }


    @AfterAll
    void tearDown() {
        // Any cleanup code if necessary
    }
}
