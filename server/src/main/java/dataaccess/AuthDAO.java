package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;

    AuthData clear() throws DataAccessException;

    void delete(String authToken);
}
