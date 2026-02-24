package dataaccess;

import model.*;

import java.util.ArrayList;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;

    UserData createUser(String username, String password, String email) throws DataAccessException;

    UserData clear() throws DataAccessException;

    ArrayList<UserData> getUsers();
}