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
        if (username == null|| password == null || email == null){
            RegisterResponse response = new RegisterResponse();
            response.setMessage("Error Bad Request");

            return response;

        }



        if (userDAO.getUser(username) == null){
            userDAO.createUser(username,password,email);
            AuthData authData = authDAO.createAuth(username);
            RegisterResponse response = new RegisterResponse();
            response.setAuthToken(authData.authToken());
            response.setUsername(authData.username());

            return response;


        }
        else {

            RegisterResponse response = new RegisterResponse();
            response.setMessage("Error Username Already taken");

            return response;

        }



    }
    public LoginResponse login(Request.LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
       if (username == null|| password == null){
           LoginResponse response = new LoginResponse();
           response.setMessage("Error Bad Request");

           return response;

       }
       UserData user = userDAO.getUser(username);


       if (userDAO.getUser(username) == null){
           LoginResponse response = new LoginResponse();
           response.setMessage("Error Unauthorised");
           return response;
       } else if (!Objects.equals(user.password(), password)) {
           LoginResponse response = new LoginResponse();
           response.setMessage("Error Unauthorised");
           return response;
       }
       AuthData authData = authDAO.createAuth(username);
       LoginResponse response = new LoginResponse();
       response.setAuthToken(authData.authToken());
       response.setUsername(authData.username());
        return response;

   }
    public LogoutResponse logout(LogoutRequest logoutRequest) throws DataAccessException{
        authDAO.delete(logoutRequest.getAuthToken());
        return new LogoutResponse();
    }
    public Boolean verify(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) != null){return Boolean.TRUE;}
        else {return Boolean.FALSE;}
    }
}