package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class UserMemoryDataAccess implements UserDAO{
    ArrayList<UserData> users = new ArrayList<>();

    public UserData getUser(String username) throws DataAccessException {
       // System.out.println("called get user");
        //System.out.println(users);
        for(UserData user: users){
            if (Objects.equals(user.username(), username)){
       //         System.out.printf("Found User %s", user.username());
                return user;
            }
        }
  //     System.out.printf("This is the array of users %s",users);
        return null;
    }


    public UserData createUser(String username, String password, String email) throws DataAccessException {
        UserData user = new UserData(username, password, email);
    //    System.out.printf("adding this user %s", user);
        users.add(user);

        return null;
    }

    @Override
    public UserData clear() throws DataAccessException {
    //    System.out.println("attempting to clear");

        users.clear();

        return null;
    }


}
