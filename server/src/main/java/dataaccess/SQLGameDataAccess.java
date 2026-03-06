package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class SQLGameDataAccess implements GameDAO{

    public SQLGameDataAccess() throws DataAccessException {
        configureDatabase();
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `json` LONGTEXT,
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
    public GameData getGame(String id) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from games where id =?");
            ps.setString(1,id);
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    return null;
                }
                String whiteUsernameR = "bob";
                String gameID = null;
                String blackUsernameR = null;
                String gameNameR = null;
                ChessGame gameInfo = null;
                while (rs.next()) {
                    gameID = rs.getString("id");
                    whiteUsernameR = rs.getString("whiteUsername");
                    blackUsernameR = rs.getString("blackUsername");
                    gameNameR = rs.getString("gameName");
                    String data = rs.getString("json");
                    gameInfo = new Gson().fromJson(data, ChessGame.class);
                }
                return new GameData(gameID, whiteUsernameR, blackUsernameR, gameNameR, gameInfo);
            }



        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> listGame() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from games");
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()){
                    String id = String.valueOf(rs.getInt("id"));
                    String whiteUsername =rs.getString("whiteUsername");
                    String blackUsername =rs.getString("blackUsername");
                    String gameName =rs.getString("gameName");
                    String data = rs.getString("json");

                    chess.ChessGame chessGame = new Gson().fromJson(data, ChessGame.class);
                    GameData game = new GameData(id,whiteUsername,blackUsername,gameName,chessGame);
                    games.add(game);

                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return games;

    }

    @Override
    public String createGame(String gameName) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName,json) VALUES (?, ?, ?,?)",PreparedStatement.RETURN_GENERATED_KEYS);


            ps.setString(1,null);
            ps.setString(2,null);
            ps.setString(3,gameName);
            String json = new Gson().toJson(new ChessGame());
            ps.setString(4,json);
            ps.executeUpdate();


            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
            else throw new DataAccessException("No index returned");
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }



    }

    @Override
    public void updateGame(String gameID, String whiteUsername, String blackUsername, String gameName, ChessGame data) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("UPDATE games SET json=?,whiteUsername=?,blackUserName=? WHERE id=?");
            String json = new Gson().toJson(data);
            ps.setString(1, json);
            ps.setString(2,whiteUsername);
            ps.setString(3,blackUsername);
            ps.setString(4, gameID);
            ps.executeUpdate();


        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement("TRUNCATE TABLE games");
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }


    }}


