package dataaccess;

import Model.AuthData;
import Model.UserData;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AuthMemoryDataAccess implements AuthDAO {
    ArrayList<AuthData> auths = new ArrayList<>();

    public AuthData getAuth(String authToken) throws DataAccessException {

        return null;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        auths.add(authData);
        System.out.println(auths);

        return authData;
    }
}