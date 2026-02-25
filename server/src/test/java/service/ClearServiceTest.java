package service;

import dataaccess.*;
import model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.*;

class ClearServiceTest {
    static final ClearService service = new ClearService();


    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new UserMemoryDataAccess();
        authDAO = new AuthMemoryDataAccess();
        gameDAO = new GameMemoryDataAccess();

        userDAO.createUser("testUser","testPassword","testEmail");
        authDAO.createAuth("testUsername");
        gameDAO.createGame("testGame");


        assertFalse(userDAO.getUsers().isEmpty());
        assertFalse(authDAO.getAuths().isEmpty());
        assertFalse(gameDAO.getGames().isEmpty());

    }
    @Test
    void clearTest() throws DataAccessException {
        service.clear(new Request.ClearRequest());

        assertTrue(userDAO.getUsers().isEmpty());
        assertTrue(authDAO.getAuths().isEmpty());
        assertTrue(gameDAO.getGames().isEmpty());

    }
}