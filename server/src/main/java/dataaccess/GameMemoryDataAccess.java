package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class GameMemoryDataAccess implements GameDAO {
    ArrayList<GameData> games = new ArrayList<>();
    int nextGameId = 0;

    public ArrayList<GameData> getGames() {
        return games;
    }

    @Override
    public GameData getGame(String id) {


        for (GameData game : games) {
            if (Objects.equals(game.gameID(), id)) {

                return game;
            }
        }

        return null;
    }

    @Override
    public ArrayList<GameData> listGame() {


       // System.out.println(games);
        return games;
    }

    @Override
    public String createGame(String gameName) {
        nextGameId += 1;
        GameData game = new GameData(String.valueOf(nextGameId), null, null, gameName);
        games.add(game);


        return String.valueOf(nextGameId);
    }

    public void updateGame(String gameID, String whiteUsername, String blackUsername, String gameName) {
        games.remove(getGame(gameID));
        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName);
        games.add(game);

    }

    @Override
    public void clear() {
        games.clear();
    }
}
