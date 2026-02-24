package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.*;

class ClearServiceTest {


    @BeforeEach
    void setUp() throws DataAccessException {
        UserDAO userDAO = new UserMemoryDataAccess();
        AuthDAO authDAO = new AuthMemoryDataAccess();
        GameDAO gameDAO = new GameMemoryDataAccess();

        userDAO.createUser("testUser","testPassword","testEmail");
        authDAO.createAuth("testUsername");
        gameDAO.createGame("testGame");


        assertFalse(userDAO.getUsers().isEmpty());
        assertFalse(authDAO.getAuths().isEmpty());
        assertFalse(gameDAO.getGames().isEmpty());

    }
    @Test
    void clearTest() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

        assertTrue(userDAO.getUsers().isEmpty());
        assertTrue(authDAO.getAuths().isEmpty());
        assertTrue(gameDAO.getGames().isEmpty());

    }
}