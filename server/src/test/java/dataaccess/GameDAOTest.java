package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.*;
import static server.Handler.authDAO;
import static server.Handler.gameDAO;
import static server.Handler.userDAO;


class GameDAOTest {


    @BeforeEach
    void setUp() throws DataAccessException {
        if (Objects.equals(type, "SQL")){
            userDAO = new SQLUserDataAccess();
            authDAO = new SQLAuthDataAccess();
            gameDAO = new SQLGameDataAccess();

            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        }
        else {
            userDAO = new UserMemoryDataAccess();
            authDAO = new AuthMemoryDataAccess();
            gameDAO = new GameMemoryDataAccess();
        }
    }
    @Test
    void getGame() throws DataAccessException {
        String tGameName = "testGame";
        String gameID = gameDAO.createGame(tGameName);
        GameData game = gameDAO.getGame(gameID);
        assertEquals(game.gameName(), tGameName);
    }
    @Test
    void getGameNull() throws DataAccessException {
        String tGameName = "testGame";
        gameDAO.createGame(tGameName);
        assertNull(gameDAO.getGame(null));

    }

    @Test
    void listGame() throws DataAccessException {
        String tGameName = "testGame";
        gameDAO.createGame(tGameName);
        String tGameName2 = "testGame2";
        gameDAO.createGame(tGameName2);
        Collection<GameData> games = gameDAO.listGame();

        ArrayList<String> gameNames = new ArrayList<>();
        for (GameData game : games){
            gameNames.add(game.gameName());
        }
        assertTrue(gameNames.contains("testGame"));
        assertTrue(gameNames.contains("testGame2"));
    }

    //Dose this count as a negative test case
    @Test
    void getGamesBad() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGame();
        assertTrue(games.isEmpty());
    }

    @Test
    void createGameTest() {
        String tGameName = "testGame";
        assertDoesNotThrow(  () -> gameDAO.createGame(tGameName));
    }
    @Test
    void createGameNullTest() {
        assertThrows(DataAccessException.class,  () -> gameDAO.createGame(null));
    }

    @Test
    void updateGame() {
    }

    @Test
    void clear() throws DataAccessException {
        String tGameName = "testGame";

        assertDoesNotThrow(  () -> gameDAO.createGame(tGameName));
        gameDAO.clear();
        assertNull(gameDAO.getGame(tGameName));
    }
}