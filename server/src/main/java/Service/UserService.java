package Service;

import Model.UserData;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.UserDAO;
import Model.Request.RegisterRequest;
import Model.Response.RegisterResponse;


public class UserService {


    private UserDAO userDAO = new MemoryDataAccess();


    public RegisterResponse register(RegisterRequest request) throws DataAccessException {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        if (userDAO.getUser(username) == null){
            userDAO.createUser(username,password,email);
            System.out.println("Made a new user");
            System.out.println(username);
        }


        return null;
    }
  //  public LoginResult login(LoginRequest loginRequest) {}
   // public void logout(LogoutRequest logoutRequest) {}
}