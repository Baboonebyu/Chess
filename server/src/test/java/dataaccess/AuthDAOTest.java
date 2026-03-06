package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.authDAO;

class AuthDAOTest {


    @BeforeEach
    void setUp() throws DataAccessException {
       DAOTestTools.setUpDAO();
    }



    @Test
    void getAuth() throws DataAccessException {
        String tUsername = "testUser";
        AuthData data = authDAO.createAuth(tUsername);
        AuthData result = authDAO.getAuth(data.authToken());
        assertEquals(data,result);
    }
    @Test
    void getAuthBad() throws DataAccessException {
        String tUsername = "testUser";
        AuthData data = authDAO.createAuth(tUsername);
        AuthData result = authDAO.getAuth(tUsername);
        assertNotEquals(data,result);
    }

    @Test
    void createAuth() throws DataAccessException {
        String tUsername = "testUser";
        assertDoesNotThrow(  () -> authDAO.createAuth(tUsername));
    }
    @Test
    void createAuthNull() throws DataAccessException {
        String tUsername = null;
        assertThrows(DataAccessException.class,  () -> authDAO.createAuth(tUsername));
    }

    @Test
    void clear() throws DataAccessException {
        String tUsername = "testUser";
        AuthData data = authDAO.createAuth(tUsername);
        assertNotNull(authDAO.getAuth(data.authToken()));
        authDAO.clear();
        assertNull(authDAO.getAuth(data.authToken()));
    }

    @Test
    void getAuths() throws DataAccessException {
        String tUsername = "testUser";
        AuthData data1 = authDAO.createAuth(tUsername);

        String tUsername2 = "testUser2";
        AuthData data2 = authDAO.createAuth(tUsername2);

        Collection<AuthData> auths = authDAO.getAuths();
        ArrayList<String> usernames = new ArrayList<>();
        for (AuthData auth : auths){
            usernames.add(auth.username());
        }
        assertTrue(usernames.contains("testUser"));
        assertTrue(usernames.contains("testUser2"));
    }
    //Dose this count as a negative test case
    @Test
    void getAuthsBad() throws DataAccessException {
        Collection<AuthData> auths = authDAO.getAuths();
        assertTrue(auths.isEmpty());
    }

    @Test
    void delete() throws DataAccessException {
        String tUsername = "testUser";
        String tUsername2 = "testUser2";
        AuthData data = authDAO.createAuth(tUsername);
        AuthData data2 = authDAO.createAuth(tUsername2);
        authDAO.delete(data.authToken());
        AuthData result = authDAO.getAuth(data.authToken());
        assertNotEquals(data,result);
        AuthData result2 = authDAO.getAuth(data2.authToken());
        assertEquals(data2,result2);
    }

    @Test
    void deleteBad()throws DataAccessException{
        String tUsername = "testUser";
        String tUsername2 = "testUser2";
        AuthData data = authDAO.createAuth(tUsername);
        AuthData data2 = authDAO.createAuth(tUsername2);
        authDAO.delete("badToken");
        AuthData result = authDAO.getAuth(data.authToken());
        assertEquals(data,result);
        AuthData result2 = authDAO.getAuth(data2.authToken());
        assertEquals(data2,result2);
    }
}