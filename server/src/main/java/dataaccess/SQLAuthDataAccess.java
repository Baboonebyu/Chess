package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SQLAuthDataAccess implements AuthDAO{

    public SQLAuthDataAccess() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256),
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`id`)
            )"""
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error Configuring Database");
        }
    }


    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from auths where authToken =?");
            ps.setString(1,authToken);
            try (ResultSet rs = ps.executeQuery()) {
                String authTokenR= null;
                String usernameR = null;

                if (!rs.isBeforeFirst() ) {
                    return null;
                }
                while(rs.next()) {
                    authTokenR = rs.getString("authToken");
                    usernameR = rs.getString("username");

                }
                return new AuthData(authTokenR,usernameR);
            }

        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES (?, ?)");

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, username);
            ps.setString(1,authToken);
            ps.setString(2,username);

            ps.executeUpdate();

            return authData;

        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("TRUNCATE TABLE auths");
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<AuthData> getAuths() throws DataAccessException {


        ArrayList<AuthData> auths = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from auths");
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()){
                    int pos = 2;
                    AuthData auth = new AuthData(rs.getString(pos),rs.getString(pos+1));
                    auths.add(auth);

                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return auths;
    }

    @Override
    public void delete(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("DELETE FROM auths where authToken =?");
            ps.setString(1, authToken);
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
