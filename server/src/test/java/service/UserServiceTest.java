package service;

import dataaccess.*;
import model.AuthData;
import model.Request;
import model.Response;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Handler;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.userDAO;
import static server.Handler.*;
class UserServiceTest {

    final static UserService service = new UserService();

    @BeforeEach
    void setUp() {
        userDAO = new UserMemoryDataAccess();
        authDAO = new AuthMemoryDataAccess();
        gameDAO = new GameMemoryDataAccess();
    }




    @Test
    void registerTest() throws DataAccessException {
        String tUsername = "testUser";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        Request.RegisterRequest request = new Request.RegisterRequest();
        request.setUsername(tUsername);
        request.setPassword(tPassword);
        request.setEmail(tEmail);

        service.register(request);
        UserData user = userDAO.getUser(tUsername);
        assertEquals(tUsername, user.username());
        assertEquals(tPassword, user.password());
        assertEquals(tEmail, user.email());
    }
    @Test
    void badRegisterTest() throws DataAccessException {
        String tUsername ="";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
      //  assertThrows(userDAO.createUser(tUsername,tPassword,tEmail), new DataAccessException());

    }

    @Test
    void logout() throws DataAccessException {
        String tUsername = "testUser";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        Request.RegisterRequest registerRequest = new Request.RegisterRequest();
        registerRequest.setUsername(tUsername);
        registerRequest.setPassword(tPassword);
        registerRequest.setEmail(tEmail);

        //create a user, auto logged in, Test that it worked
        Response.RegisterResponse response = service.register(registerRequest);
        String token = response.getAuthToken();
        assertNotNull(authDAO.getAuth(token));

        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(token);
        service.logout(logoutRequest);
        assertNull(authDAO.getAuth(token));
    }

    @Test
    void login() throws DataAccessException {



    }


    @Test
    void verify() {
    }
}