package service;

import model.AuthData;
import model.Request;
import model.UserData;
import dataaccess.*;

import model.Request.*;
import model.Response.*;

import java.util.Objects;

import static server.Handler.authDAO;
import static server.Handler.userDAO;


public class UserService {


    public RegisterResponse register(RegisterRequest request) throws DataAccessException {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        if (username == null || password == null || email == null) {
            RegisterResponse response = new RegisterResponse();
            throw new BadRequestException("Error Bad Request");
        }


        if (userDAO.getUser(username) == null) {
            userDAO.createUser(username, password, email);
            AuthData authData = authDAO.createAuth(username);
            RegisterResponse response = new RegisterResponse();
            response.setAuthToken(authData.authToken());
            response.setUsername(authData.username());

            return response;


        } else {

            RegisterResponse response = new RegisterResponse();
            throw new AlreadyTakenException("Error: already taken");
        }


    }

    public LoginResponse login(Request.LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if (username == null || password == null) {
            throw new BadRequestException("Error Bad Request");

        }
        UserData user = userDAO.getUser(username);


        if (userDAO.getUser(username) == null) {
            throw new UnauthorisedException("Error Unauthorised");
        } else if (!Objects.equals(user.password(), password)) {
            throw new UnauthorisedException("Error Unauthorised");
        }
        AuthData authData = authDAO.createAuth(username);
        LoginResponse response = new LoginResponse();
        response.setAuthToken(authData.authToken());
        response.setUsername(authData.username());
        return response;

    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws DataAccessException {
        String token = logoutRequest.getAuthToken();
        verify(token);
        authDAO.delete(token);
        return new LogoutResponse();
    }

    public Boolean verify(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) != null) {
            return Boolean.TRUE;
        } else {
            throw new UnauthorisedException("Error: unauthorized");
        }
    }
}