package org.otabek.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.otabek.dao.IUserDAO;
import org.otabek.entity.Role;
import org.otabek.entity.User;
import org.otabek.exceptions.DaoException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private IUserDAO userDAOMock;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAOMock = Mockito.mock(IUserDAO.class);
        userService = new UserService(userDAOMock);
    }

    @Test
    public void testCreateUserSuccess() throws DaoException {
        User newUser = new User(0, "otabek", "passwordTest", Role.USER);

        when(userDAOMock.findUserByUsername("otabek")).thenReturn(null);

        when(userDAOMock.createUser(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1);
            return u;
        });

        User createdUser = userService.createUser("otabek", "passwordTest", Role.USER);
        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("otabek", createdUser.getUsername());
    }

    @Test
    public void testCreateUserFail() throws DaoException {
        User existingUser = new User(1, "otabek", "passwordTest", Role.USER);

        when(userDAOMock.findUserByUsername("otabek")).thenReturn(existingUser);

        User createdUser = userService.createUser("otabek", "passwordTest", Role.USER);
        assertNull(createdUser);
    }

    @Test
    public void testAuthenticateSuccess() throws DaoException {
        User existingUser = new User(1, "otabek", "passwordTest", Role.USER);

        when(userDAOMock.findUserByUsername("otabek")).thenReturn(existingUser);

        User authenticatedUser = userService.authenticate("otabek", "passwordTest");
        assertNotNull(authenticatedUser);
        assertEquals(1, authenticatedUser.getId());
        assertEquals("otabek", authenticatedUser.getUsername());
    }

    @Test
    public void testAuthenticateFail() throws DaoException {
        User existingUser = new User(1, "otabek", "passwordTest", Role.USER);

        when(userDAOMock.findUserByUsername("otabek")).thenReturn(existingUser);

        User authenticatedUser = userService.authenticate("otabek", "wrongPassword");
        assertNull(authenticatedUser);
    }

    @Test
    public void testFindUserByUsername() throws DaoException {
        User existingUser = new User(1, "otabek", "passwordTest", Role.USER);

        when(userDAOMock.findUserByUsername("otabek")).thenReturn(existingUser);

        User foundUser = userService.findUserByUsername("otabek");
        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
        assertEquals("otabek", foundUser.getUsername());
    }

    @Test
    public void testDeleteUser() throws DaoException {
        when(userDAOMock.deleteUser(1)).thenReturn(true);

        boolean isDeleted = userService.deleteUser(1);
        assertTrue(isDeleted);
    }

    @Test
    public void testListAllUsers() throws DaoException {
        User user1 = new User(1, "otabek", "passwordTest", Role.USER);
        User user2 = new User(2, "john", "passwordTest", Role.USER);

        when(userDAOMock.findAllUsers()).thenReturn(java.util.List.of(user1, user2));

        assertEquals(2, userService.listAllUsers().size());
    }

    @Test
    public void testListAllUsersEmpty() throws DaoException {
        when(userDAOMock.findAllUsers()).thenReturn(java.util.List.of());

        assertEquals(0, userService.listAllUsers().size());
    }
}
