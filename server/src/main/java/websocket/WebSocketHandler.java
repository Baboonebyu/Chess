package websocket;

import chess.ChessBoard;
import com.google.gson.Gson;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.Handler;
import service.GameService;
import service.UnauthorisedException;
import service.UserService;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommand.*;
import java.io.IOException;
import java.util.Objects;

import static server.Handler.gameDAO;
import com.google.gson.Gson;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    UserService userService = new UserService();
    GameService gameService = new GameService();
    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> Connect(command, ctx.session);
                case LEAVE -> Leave(command, ctx.session);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void Leave(UserGameCommand command, Session session) throws IOException, DataAccessException {

        String username = command.getUserName();
        String returnString = username+" has left the game.";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);

        String stringGameId = String.valueOf(command.getGameID());
        GameData game = gameDAO.getGame(String.valueOf(stringGameId));
        if (Objects.equals(game.whiteUsername(), username)){
            gameDAO.updateGame(stringGameId,null,game.blackUsername(),game.gameName(),game.game());
        }
        else if (Objects.equals(game.blackUsername(), username)){
            gameDAO.updateGame(stringGameId,game.whiteUsername(),null,game.gameName(),game.game());
        }

        connections.remove(session,command.getGameID());
        connections.broadcast(session,message, command.getGameID());
    }

    private void Connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        String authToken = command.getAuthToken();
        String stringGameId = String.valueOf(command.getGameID());

        try{
            userService.verify(authToken);


            if (gameDAO.getGame(stringGameId) == null){
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errorMessage.setErrorMessage("Error Bad game ID");
                String msg = errorMessage.toString();

                session.getRemote().sendString(msg);
                return;
            }





        connections.add(session,command.getGameID());
        String username = command.getUserName();
        String returnString = username+" has joined the game.";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);
        connections.broadcast(session,message, command.getGameID());



        Gson gson = new Gson();
        String game= gson.toJson(gameDAO.getGame(stringGameId));

        ServerMessage gameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        gameMessage.setGame(game);

        String msg = gameMessage.toString();

        session.getRemote().sendString(msg);


        }
        catch (UnauthorisedException e){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error Unauthorized");
            String msg = errorMessage.toString();

            session.getRemote().sendString(msg);

        }
    }


    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }






   //TODO add methods here
}