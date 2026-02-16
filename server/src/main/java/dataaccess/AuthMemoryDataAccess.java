package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AuthMemoryDataAccess implements AuthDAO {
    ArrayList<AuthData> auths = new ArrayList<>();

    public AuthData getAuth(String authToken) throws DataAccessException {

        for(AuthData auth: auths){
            if (Objects.equals(auth.authToken(), authToken)){

                return auth;
            }
        }
        return null;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        auths.add(authData);
       // System.out.println(auths);

        return authData;
    }


    public AuthData clear() throws DataAccessException {
     //   System.out.println("clearing Auths");
        auths.clear();
        return null;
    }


    public void delete(String authToken) {
        auths.removeIf(auth -> Objects.equals(auth.authToken(), authToken));
    }
}