package dataaccess;

import Model.AuthData;
import Model.UserData;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

import java.util.ArrayList;
import java.util.Objects;

public class AuthMemoryDataAccess implements AuthDAO {
    ArrayList<AuthData> Auths = new ArrayList<>();

    public AuthData getAuth(String authToken) throws DataAccessException {

        return null;
    }

    public AuthData createAuth(String username) throws DataAccessException {


        return null;
    }
}