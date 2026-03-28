package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    GameData getGame(String id) throws DataAccessException;

    ArrayList<GameData> listGame() throws DataAccessException;

    String createGame(String gameName) throws DataAccessException;

    void updateGame(String gameID, String whiteUsername, String blackUsername, String gameName, ChessGame data, boolean isOver) throws DataAccessException;

    void clear() throws DataAccessException;
}
