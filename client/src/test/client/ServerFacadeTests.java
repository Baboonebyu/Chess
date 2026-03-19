package client;

import model.Request;
import model.Response;
import org.junit.jupiter.api.*;
import server.Server;
import sharedServerFiles.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade = null;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:"+Integer.toString(port);
         facade = new ServerFacade(url);

    }

    @BeforeEach
            public void clear() throws Exception
    {   facade.clearDatabase(new Request.ClearRequest());}


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        Request.RegisterRequest request = new Request.RegisterRequest();
        request.setUsername("player1");
        request.setPassword("password");
        request.setEmail("p1@email.com");
        Response.RegisterResponse response = facade.registerUser(request);

        assertTrue(response.getAuthToken().length() > 10);
    }

    @Test
    void registerSameUser() throws Exception{
        Request.RegisterRequest request = new Request.RegisterRequest();
        request.setUsername("player1");
        request.setPassword("password");
        request.setEmail("p1@email.com");
        facade.registerUser(request);
        assertThrows(Exception.class ,()-> facade.registerUser(request));
    }

    @Test
    void logout() throws Exception{
        String token = createUser("player1","password","email");

        Request.LogoutRequest request = new Request.LogoutRequest(token);
        assertDoesNotThrow(()-> facade.logoutUser(request, token));

    }
    @Test
    void logoutBadToken() throws Exception{
        String token = createUser("player1","password","email");


        Request.LogoutRequest request = new Request.LogoutRequest("token");
        assertThrows(Exception.class,()-> facade.logoutUser(request, "token"));

    }

    @Test
    void login() throws Exception{
        //set up

        String token = createUser("player1","password","email");

        Request.LogoutRequest lRequest = new Request.LogoutRequest(token);
        facade.logoutUser(lRequest, token);

        //login
        Request.LoginRequest request = new Request.LoginRequest();
        request.setUsername("player1");
        request.setPassword("password");
        Response.LoginResponse response = facade.loginUser(request);
        assertNotEquals(token,response.getAuthToken());

    }

    @Test
    void loginBadUsername() throws Exception{
        //set up
        String token = createUser("player1","password","email");

        //login
        Request.LoginRequest request = new Request.LoginRequest();
        request.setUsername("bad");
        request.setPassword("password");
        assertThrows(Exception.class , ()-> facade.loginUser(request));


    }

    @Test
    void createGame()throws Exception{
        Request.CreateGameRequest request = new Request.CreateGameRequest();
        request.setGameName("gameName");
        String token = createUser("Username","Password","Email");
        assertDoesNotThrow(()->facade.createGame(request, token));

    }
    @Test
    void createGameBadAuth()throws Exception{
        Request.CreateGameRequest request = new Request.CreateGameRequest();
        request.setGameName("gameName");
        createUser("Username","Password","Email");
        assertThrows(Exception.class,()->facade.createGame(request, "token"));
    }


    @Test
    void listGames() throws Exception{
        Request.CreateGameRequest cRequest = new Request.CreateGameRequest();
        cRequest.setGameName("gameName");
        String token = createUser("Username","Password","Email");
        facade.createGame(cRequest, token);
        Request.CreateGameRequest cRequest2 = new Request.CreateGameRequest();
        cRequest2.setGameName("gameName2");
        facade.createGame(cRequest2, token);

        //listGames
        Request.ListGamesRequest request = new Request.ListGamesRequest();
        Response.ListGamesResponse response = facade.listGames(request, token);
        assertEquals(2, response.getGames().size());
    }
    @Test
    void listGamesBadToken() throws Exception{
        Request.CreateGameRequest cRequest = new Request.CreateGameRequest();
        cRequest.setGameName("gameName");
        String token = createUser("Username","Password","Email");
        facade.createGame(cRequest, token);
        Request.CreateGameRequest cRequest2 = new Request.CreateGameRequest();
        cRequest2.setGameName("gameName2");
        facade.createGame(cRequest2, token);

        //listGames
        Request.ListGamesRequest request = new Request.ListGamesRequest();
        assertThrows(Exception.class,()->facade.listGames(request, "BadToken"));

    }










    String createUser(String username,String password, String email) throws Exception {
        Request.RegisterRequest rRequest = new Request.RegisterRequest();
        rRequest.setUsername(username);
        rRequest.setPassword(password);
        rRequest.setEmail(email);
        Response.RegisterResponse lResponse = facade.registerUser(rRequest);
        return lResponse.getAuthToken();
    }
}

