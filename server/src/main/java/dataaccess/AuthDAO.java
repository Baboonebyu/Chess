package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;

    AuthData clear() throws DataAccessException;

    ArrayList<AuthData> getAuths();

    void delete(String authToken);
}
