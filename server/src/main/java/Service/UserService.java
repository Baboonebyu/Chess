package Service;

import Model.Response;
import dataaccess.DataAccessException;
import dataaccess.UserMemoryDataAccess;
import dataaccess.UserDAO;
import Model.Request.RegisterRequest;
import Model.Response.RegisterResponse;


public class UserService {


    private UserDAO userDAO = new UserMemoryDataAccess();


    public RegisterResponse register(RegisterRequest request) throws DataAccessException {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        if (userDAO.getUser(username) == null){
            userDAO.createUser(username,password,email);
            //System.out.println("Made a new user");
           // System.out.println(username);

        }
        else {

            RegisterResponse response = new RegisterResponse();
            response.setMessage("Error Username Already taken");

            return response;

        }


        return null;
    }
  //  public LoginResult login(LoginRequest loginRequest) {}
   // public void logout(LogoutRequest logoutRequest) {}
}