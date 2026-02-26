package service;

import model.GameData;
import model.Request.*;
import model.Response.*;

import dataaccess.DataAccessException;

import java.util.Objects;

import static server.Handler.gameDAO;

public class GameService {





    public CreateGameResponse create(CreateGameRequest request) throws DataAccessException {

        String gameName = request.getGameName();
       // System.out.printf("this is the game name %s",gameName);
        CreateGameResponse response = new CreateGameResponse();

        if(request.getGameName() == null){
            throw new BadRequestException("Error: Bad Request");
        }
        else{
            String gameID = gameDAO.createGame(gameName);
            response.setGameID(gameID);
        }
        return response;
    }

    public ListGamesResponse list(ListGamesRequest request) {
        ListGamesResponse response = new ListGamesResponse();
        response.setGames( gameDAO.listGame());

        return response;
    }

    public JoinGameResponse join(JoinGameRequest request, String userName) {
        String gameID = request.getGameID();
        String playerColor = request.getPlayerColor();
        GameData game = gameDAO.getGame(gameID);
        JoinGameResponse response = new JoinGameResponse();
        if (game == null){
            throw new BadRequestException("Error: Bad Request");
        }
        if(!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK")){
            throw new BadRequestException("Error: Bad Request");
        }

        if (Objects.equals(playerColor, "WHITE") && game.whiteUsername()==null){
           gameDAO.updateGame(gameID,userName,game.blackUsername(),game.gameName());
        } else if (Objects.equals(playerColor, "BLACK") && game.blackUsername()==null) {
            gameDAO.updateGame(gameID,game.whiteUsername(),userName,game.gameName());
        }
        else{
            throw new AlreadyTakenException("Error: already taken");
        }
        return null;
    }
}
