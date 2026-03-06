package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLUserDataAccess implements UserDAO {

    public SQLUserDataAccess() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`)
            )"""
        };
        DatabaseManager.configureDatabase(createStatements);
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("select * from users where username =?");
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                String usernameR = null;
                String passwordE = null;
                String emailR = null;

                if (!rs.isBeforeFirst()) {
                    return null;
                }
                while (rs.next()) {
                    usernameR = rs.getString("username");
                    passwordE = rs.getString("password");
                    emailR = rs.getString("email");
                }
                return new UserData(usernameR, passwordE, emailR);
            }


        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");

            String hashedPW = BCrypt.hashpw(password, BCrypt.gensalt());
            ps.setString(1, username);
            ps.setString(2, hashedPW);
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }


        return null;
    }

    @Override
    public UserData clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("TRUNCATE TABLE users");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<UserData> getUsers() throws DataAccessException {
        ArrayList<UserData> users = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("select * from users");
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int pos = 2;
                    UserData user = new UserData(rs.getString(pos), rs.getString(pos + 1), rs.getString(pos + 2));
                    users.add(user);

                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return users;
    }
}
