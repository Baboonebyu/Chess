package dataaccess;

import Model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    GameData getGame(String id);
    ArrayList<GameData> listGame();
    String createGame(String gameName);
    void updateGame(String gameID, String whiteUsername, String blackUsername, String gameName);
    void clear();
}
