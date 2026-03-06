package dataaccess;

import java.util.Objects;

import static server.Handler.*;
import static server.Handler.authDAO;
import static server.Handler.gameDAO;
import static server.Handler.userDAO;

public class DAOtestTools {
    public static void DAOSetUp() throws DataAccessException {
        if (Objects.equals(type, "SQL")){
            userDAO = new SQLUserDataAccess();
            authDAO = new SQLAuthDataAccess();
            gameDAO = new SQLGameDataAccess();

            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        }
        else {
            userDAO = new UserMemoryDataAccess();
            authDAO = new AuthMemoryDataAccess();
            gameDAO = new GameMemoryDataAccess();
        }
    }
}
