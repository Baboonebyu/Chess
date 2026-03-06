package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static server.Handler.*;

class UserDAOTest {


    @BeforeEach
    void setUp() throws DataAccessException {
        DAOtestTools.DAOSetUp();
    }

    @Test
    void createUserTest() {
        String tUsername = "testUser";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
       assertDoesNotThrow(  () -> userDAO.createUser(tUsername,tPassword,tEmail));
    }
    @Test
    //null username
    void badCreateUserTest() {
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        assertThrows(DataAccessException.class,  () -> userDAO.createUser(null,tPassword,tEmail));
    }



    @Test
    void getUserTest() throws DataAccessException {
        String tUsername = "testUser";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        assertDoesNotThrow(  () -> userDAO.createUser(tUsername,tPassword,tEmail));
        UserData user = userDAO.getUser(tUsername);
        assertEquals(tUsername, user.username());
        assertTrue(BCrypt.checkpw(tPassword,user.password()));
        assertEquals(tEmail,user.email());
    }
    @Test
    void badGetUserTest() throws DataAccessException {
        String tUsername = "testUser";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        assertDoesNotThrow(  () -> userDAO.createUser(tUsername,tPassword,tEmail));
        assertNull(userDAO.getUser("testUserBad"));
    }


    @Test
    void clear() throws DataAccessException {
        String tUsername = "testUser";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        assertDoesNotThrow(  () -> userDAO.createUser(tUsername,tPassword,tEmail));
        userDAO.clear();
        assertNull(userDAO.getUser(tUsername));
    }

    @Test
    void getUsers() throws DataAccessException {
        String tUsername = "testUser";
        String tPassword = "testPassword";
        String tEmail = "testEmail";
        assertDoesNotThrow(  () -> userDAO.createUser(tUsername,tPassword,tEmail));
        String tUsername2 = "testUser2";
        String tPassword2 = "testPassword2";
        String tEmail2 = "testEmail2";
        assertDoesNotThrow(  () -> userDAO.createUser(tUsername2,tPassword2,tEmail2));

        Collection<UserData> users = userDAO.getUsers();
        ArrayList<String> usernames = new ArrayList<>();
        for (UserData user : users){
            usernames.add(user.username());
        }
        assertTrue(usernames.contains("testUser"));
        assertTrue(usernames.contains("testUser2"));
    }

    //Dose this count as a negative test case
    @Test
    void getUsersBad() throws DataAccessException {
        Collection<UserData> users = userDAO.getUsers();
        assertTrue(users.isEmpty());
    }
}