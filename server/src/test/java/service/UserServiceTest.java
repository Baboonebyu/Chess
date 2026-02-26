package service;

import dataaccess.*;
import model.Request;
import model.Response;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.userDAO;
import static server.Handler.*;

class UserServiceTest {

    final static UserService USER_SERVICE = new UserService();
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

        USER_SERVICE.register(request);
        UserData user = userDAO.getUser(tUsername);
        assertEquals(tUsername, user.username());
        assertEquals(tPassword, user.password());
        assertEquals(tEmail, user.email());
    }

    @Test
    void badRegisterTest() throws DataAccessException {
        String tUsername = null;
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        Request.RegisterRequest request = new Request.RegisterRequest();
        request.setUsername(tUsername);
        request.setPassword(tPassword);
        request.setEmail(tEmail);
        assertThrows(BadRequestException.class, () -> USER_SERVICE.register(request));

    }

    @Test
    void logoutTest() throws DataAccessException {
        String token = createUserForTest();

        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(token);
        USER_SERVICE.logout(logoutRequest);
        assertNull(authDAO.getAuth(token));
    }

    @Test
    void logoutBadTest() throws DataAccessException {
        createUserForTest();
        String badToken = "BadToken";
        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(badToken);
        assertThrows(UnauthorisedException.class, () -> USER_SERVICE.logout(logoutRequest));
    }


    @Test
    void loginTest() throws DataAccessException {
        String token = createUserForTest();

        //logs out the user so we can log in
        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(token);
        USER_SERVICE.logout(logoutRequest);
        assertNull(authDAO.getAuth(token));

        Request.LoginRequest loginRequest = new Request.LoginRequest();
        loginRequest.setUsername(tUsername);
        loginRequest.setPassword(tPassword);
        Response.LoginResponse response = USER_SERVICE.login(loginRequest);
        String rToken = response.getAuthToken();
        assertNotNull(authDAO.getAuth(rToken));


    }

    @Test
    void loginBadTest() throws DataAccessException {
        String token = createUserForTest();

        //logs out the user so we can log in
        Request.LogoutRequest logoutRequest = new Request.LogoutRequest(token);
        USER_SERVICE.logout(logoutRequest);
        assertNull(authDAO.getAuth(token));
        Request.LoginRequest loginRequest = new Request.LoginRequest();
        loginRequest.setUsername(tUsername);
        loginRequest.setPassword("BadPassword");
        assertThrows(UnauthorisedException.class, () -> USER_SERVICE.login(loginRequest));

    }


    @Test
    void verifyTest() throws DataAccessException {
        String token = createUserForTest();

        assertEquals(true, USER_SERVICE.verify(token));
    }

    @Test
    void verifyBadTokenTest() throws DataAccessException {
        createUserForTest();


        String badToken = "BadToken";
        assertThrows(UnauthorisedException.class, () -> USER_SERVICE.verify(badToken));
    }

    String createUserForTest() throws DataAccessException {

        Request.RegisterRequest registerRequest = new Request.RegisterRequest();
        registerRequest.setUsername(tUsername);
        registerRequest.setPassword(tPassword);
        registerRequest.setEmail(tEmail);

        //create a user, auto logged in, Test that it worked
        Response.RegisterResponse response = USER_SERVICE.register(registerRequest);
        String token = response.getAuthToken();
        assertNotNull(authDAO.getAuth(token));
        return token;
    }
}