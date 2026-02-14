package dataaccess;

import Model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;
}
