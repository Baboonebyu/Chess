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
        Request.RegisterRequest rRequest = new Request.RegisterRequest();
        rRequest.setUsername("player1");
        rRequest.setPassword("password");
        rRequest.setEmail("p1@email.com");
        Response.RegisterResponse response = facade.registerUser(rRequest);
        String token = response.getAuthToken();

        Request.LogoutRequest request = new Request.LogoutRequest(token);
        assertDoesNotThrow(()-> facade.logoutUser(request, token));

    }
    @Test
    void logoutBadToken() throws Exception{
        Request.RegisterRequest rRequest = new Request.RegisterRequest();
        rRequest.setUsername("player1");
        rRequest.setPassword("password");
        rRequest.setEmail("p1@email.com");
        Response.RegisterResponse response = facade.registerUser(rRequest);
        String token = response.getAuthToken();

        Request.LogoutRequest request = new Request.LogoutRequest("token");
        assertThrows(Exception.class,()-> facade.logoutUser(request, "token"));

    }
}
