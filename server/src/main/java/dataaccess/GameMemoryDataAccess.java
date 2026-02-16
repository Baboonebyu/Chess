package dataaccess;

import Model.AuthData;
import Model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class GameMemoryDataAccess implements GameDAO{
    ArrayList<GameData> games = new ArrayList<>();
    int NextGameId = 0;

    @Override
    public GameData getGame(String id) {


            for(GameData game: games){
                if (Objects.equals(game.gameId(), id)){

                    return game;
                }
            }

        return null;
    }

    @Override
    public ArrayList<GameData> listGame() {
        return games;
    }

    @Override
    public String createGame(String gameName) {
        NextGameId +=1;
        GameData game = new GameData(String.valueOf(NextGameId),null,null,gameName);
        games.add(game);


        return String.valueOf(NextGameId);
    }
    public void updateGame(String gameID, String whiteUsername, String blackUsername, String gameName){
        games.remove(getGame(gameID));
        GameData game = new GameData(gameID,whiteUsername,blackUsername,gameName);
        games.add(game);

    }

    @Override
    public void clear() {
        games.clear();
    }
}
