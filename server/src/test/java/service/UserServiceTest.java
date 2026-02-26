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
    String tUsername = "testUser";
    String tPassword = "testPassword";
    String tEmail = "testEmail";

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
        String tUsername =null;
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        Request.RegisterRequest request = new Request.RegisterRequest();
        request.setUsername(tUsername);
        request.setPassword(tPassword);
        request.setEmail(tEmail);
      assertThrows(BadRequestException.class,()->service.register(request) );

    }

    @Test
    void logoutTest() throws DataAccessException {
        String token = createUserForTest();

        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(token);
        service.logout(logoutRequest);
        assertNull(authDAO.getAuth(token));
    }

    @Test
    void logoutBadTest() throws DataAccessException {
        createUserForTest();
        String badToken = "BadToken";
        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(badToken);
        assertThrows(UnauthorisedException.class,()->service.logout(logoutRequest) );
    }



    @Test
    void loginTest() throws DataAccessException {
        String token = createUserForTest();

        //logs out the user so we can log in
        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(token);
        service.logout(logoutRequest);
        assertNull(authDAO.getAuth(token));

        Request.LoginRequest loginRequest = new Request.LoginRequest();
        loginRequest.setUsername(tUsername);
        loginRequest.setPassword(tPassword);
        Response.LoginResponse response = service.login(loginRequest);
        String rToken = response.getAuthToken();
        assertNotNull( authDAO.getAuth(rToken));


    }
    @Test
    void loginBadTest() throws DataAccessException {
        String token = createUserForTest();

        //logs out the user so we can log in
        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(token);
        service.logout(logoutRequest);
        assertNull(authDAO.getAuth(token));
        Request.LoginRequest loginRequest = new Request.LoginRequest();
        loginRequest.setUsername(tUsername);
        loginRequest.setPassword("BadPassword");
        assertThrows(UnauthorisedException.class, () -> service.login(loginRequest));

    }


    @Test
    void verifyTest() throws DataAccessException {
        String token = createUserForTest();

        assertEquals(true, service.verify(token));
    }

    @Test
    void verifyBadTokenTest() throws DataAccessException {
        createUserForTest();


        String badToken = "BadToken";
        assertThrows(UnauthorisedException.class, ()-> service.verify(badToken));
    }

    String createUserForTest() throws DataAccessException {

        Request.RegisterRequest registerRequest = new Request.RegisterRequest();
        registerRequest.setUsername(tUsername);
        registerRequest.setPassword(tPassword);
        registerRequest.setEmail(tEmail);

        //create a user, auto logged in, Test that it worked
        Response.RegisterResponse response = service.register(registerRequest);
        String token = response.getAuthToken();
        assertNotNull(authDAO.getAuth(token));
        return token;
    }
}