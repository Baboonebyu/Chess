package dataaccess;

import Model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryDataAccess implements UserDAO{
    ArrayList<UserData> users = new ArrayList<>();

    public UserData getUser(String username) throws DataAccessException {
        for(UserData user: users){
            if (Objects.equals(user.username(), username)){
                return user;
            }
        }
        return null;
    }


    public UserData createUser(String username, String password, String email) throws DataAccessException {
        UserData user = new UserData(username, password, email);
        users.add(user);
        return null;
    }
}
