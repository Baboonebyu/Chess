package service;

import dataaccess.AuthMemoryDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameMemoryDataAccess;
import dataaccess.UserMemoryDataAccess;
import model.GameData;
import model.Request;
import model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.*;

class GameServiceTest {

    final static UserService USER_SERVICE = new UserService();
    final static GameService GAME_SERVICE = new GameService();
    String t1Username = "testUser1";
    String t1Password = "testPassword1";
    String t1Email = "testEmail1";
    String t2Username = "testUser2";
    String t2Password = "testPassword2";
    String t2Email = "testEmail2";

    @BeforeEach
    void setUp() {
        userDAO = new UserMemoryDataAccess();
        authDAO = new AuthMemoryDataAccess();
        gameDAO = new GameMemoryDataAccess();
    }

    @Test
    void createTest() throws DataAccessException {
        createUserForTest(t1Username, t1Password, t1Email);
        Request.CreateGameRequest request = new Request.CreateGameRequest();
        request.setGameName("test game");
        Response.CreateGameResponse response = GAME_SERVICE.create(request);

        String gameID = response.getGameID();
        assertNotNull(gameDAO.getGame(gameID));
    }

    @Test
    void createBadTest() throws DataAccessException {
        createUserForTest(t1Username, t1Password, t1Email);
        Request.CreateGameRequest request = new Request.CreateGameRequest();
        assertThrows(BadRequestException.class, () -> GAME_SERVICE.create(request));


    }

    @Test
    void listTest() throws DataAccessException {
        createUserForTest(t1Username, t1Password, t1Email);
        Request.CreateGameRequest requestC = new Request.CreateGameRequest();
        requestC.setGameName("test game");
        Response.CreateGameResponse responseC = GAME_SERVICE.create(requestC);
        Request.ListGamesRequest requestL = new Request.ListGamesRequest();
        Response.ListGamesResponse response = GAME_SERVICE.list(requestL);
        Collection<GameData> games = response.getGames();
        ArrayList<String> gameNames = new ArrayList<>();
        for (GameData game : games) {
            gameNames.add(game.gameName());
        }
        assertTrue(gameNames.contains("test game"));

    }

    @Test
    void listBadTest() throws DataAccessException {
        //The List method in GameService won't return and error as the verifying happens at the handler level
        Request.ListGamesRequest requestL = new Request.ListGamesRequest();
        Response.ListGamesResponse response = GAME_SERVICE.list(requestL);
        assertTrue(response.getGames().isEmpty());
    }

    @Test
    void joinTest() throws DataAccessException {
        createUserForTest(t1Username, t1Password, t1Email);
        Request.CreateGameRequest requestC = new Request.CreateGameRequest();
        requestC.setGameName("test game");
        Response.CreateGameResponse responseC = GAME_SERVICE.create(requestC);
        Request.JoinGameRequest requestJ = new Request.JoinGameRequest();
        requestJ.setGameID(responseC.getGameID());
        requestJ.setPlayerColor("WHITE");
        GAME_SERVICE.join(requestJ, t1Username);
        GameData game = gameDAO.getGame(responseC.getGameID());
        assertEquals(t1Username, game.whiteUsername());

    }

    @Test
    void joinBadTest() throws DataAccessException {
        createUserForTest(t1Username, t1Password, t1Email);
        Request.CreateGameRequest requestC = new Request.CreateGameRequest();
        requestC.setGameName("test game");
        Response.CreateGameResponse responseC = GAME_SERVICE.create(requestC);
        Request.JoinGameRequest requestJ = new Request.JoinGameRequest();
        requestJ.setGameID(responseC.getGameID());
        requestJ.setPlayerColor("WHITE");
        GAME_SERVICE.join(requestJ, t1Username);
        GameData game = gameDAO.getGame(responseC.getGameID());
        assertEquals(t1Username, game.whiteUsername());

        Request.JoinGameRequest requestBad = new Request.JoinGameRequest();
        requestBad.setGameID(responseC.getGameID());
        requestBad.setPlayerColor("WHITE");
        assertThrows(AlreadyTakenException.class, () -> GAME_SERVICE.join(requestBad, t2Username));

    }


    String createUserForTest(String username, String password, String email) throws DataAccessException {

        Request.RegisterRequest registerRequest = new Request.RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setEmail(email);

        //create a user, auto logged in, Test that it worked
        Response.RegisterResponse response = USER_SERVICE.register(registerRequest);
        String token = response.getAuthToken();
        assertNotNull(authDAO.getAuth(token));
        return token;
    }
}