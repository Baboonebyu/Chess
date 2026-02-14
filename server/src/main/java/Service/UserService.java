package Service;

import Model.AuthData;
import Model.Response;
import dataaccess.*;
import Model.Request.RegisterRequest;
import Model.Response.RegisterResponse;


public class UserService {


    private final UserDAO userDAO = new UserMemoryDataAccess();
    private AuthDAO authDAO = new AuthMemoryDataAccess();


    public RegisterResponse register(RegisterRequest request) throws DataAccessException {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
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
  //  public LoginResult login(LoginRequest loginRequest) {}
   // public void logout(LogoutRequest logoutRequest) {}
}