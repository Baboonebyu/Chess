package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;

import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UnauthorisedException;
import service.UserService;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import static server.Handler.authDAO;
import static server.Handler.gameDAO;

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
                case CONNECT -> connect(command, ctx.session);
                case LEAVE -> leave(command, ctx.session);
                case RESIGN -> resign(command, ctx.session);
                case MAKE_MOVE -> move(command,ctx.session);
                case GET_MOVES -> getMoves(command,ctx.session);
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void getMoves(UserGameCommand command, Session session) throws DataAccessException, IOException {
        String stringGameId = String.valueOf(command.getGameID());
        GameData gamedata = gameDAO.getGame(stringGameId);
        ChessGame game = gamedata.game();
        Collection<ChessMove> moves = game.validMoves(command.getPosition());

        Gson gson = new Gson();
        String movesString = gson.toJson(moves);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.Highlight);
        message.setChessMoves(movesString);
        String msg = message.toString();
        session.getRemote().sendString(msg);

    }

    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {
        String username = getUserName(session,authDAO.getAuth(command.getAuthToken()));
        String stringGameId = String.valueOf(command.getGameID());
        GameData game = gameDAO.getGame(stringGameId);

        if (game.isOver()){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error Game over. You can't Resign");
            String msg = errorMessage.toString();

            session.getRemote().sendString(msg);
            return;
        }


        if(!Objects.equals(username, game.whiteUsername()) && !Objects.equals(username, game.blackUsername())){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error Observers can't resign");
            String msg = errorMessage.toString();

            session.getRemote().sendString(msg);
            return;
        }

        String returnString = username+" has resigned.\n The game is over.";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);
        connections.broadcast(null,message, command.getGameID());





        gameDAO.updateGame(stringGameId,game.whiteUsername(),game.blackUsername(),game.gameName(),game.game(),true);
        game = gameDAO.getGame(stringGameId);
    }


    private void move(UserGameCommand command, Session session) throws DataAccessException, IOException {
        String stringGameId = String.valueOf(command.getGameID());

        ChessMove move = command.getMove();

        AuthData authData= authDAO.getAuth(command.getAuthToken());

        String userName = getUserName(session, authData);

        model.GameData gameData = gameDAO.getGame(stringGameId);
        ChessGame game = gameData.game();

        if (gameData.isOver()){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error Game over");
            String msg = errorMessage.toString();

            session.getRemote().sendString(msg);
            return;
        }



        if(game.getTeamTurn() == ChessGame.TeamColor.WHITE && !Objects.equals(userName, gameData.whiteUsername())){
            sendError(session, "Error Not valid move");
            return;

        } else if (game.getTeamTurn() == ChessGame.TeamColor.BLACK && !Objects.equals(userName, gameData.blackUsername())) {
            sendError(session, "Error Not valid move");
            return;
        }


        //     System.out.println(game.getBoard());



        try {

            //System.out.println(move);

            game.makeMove(move);

            gameDAO.updateGame(stringGameId,gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),game, false);

            System.out.println(gameDAO.getGame(stringGameId).game().getBoard());
            if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
                checkMateHandler(gameData.whiteUsername(),command.getGameID());
            } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                checkMateHandler(gameData.blackUsername(),command.getGameID());
            }
            else if(game.isInStalemate(ChessGame.TeamColor.WHITE)){
                staleMateHandler(gameData.whiteUsername(),command.getGameID());
            } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                staleMateHandler(gameData.blackUsername(),command.getGameID());
            }
            else if(game.isInCheck(ChessGame.TeamColor.WHITE)){
                checkMessage(gameData.whiteUsername(),command.getGameID());
            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                checkMessage(gameData.blackUsername(),command.getGameID());
            }


            String returnString = "\n"+userName+" moved "+move.getStartPosition().getRow()+ "," +move.getStartPosition().getColumn()+
                    " to " + move.getEndPosition().getRow()+","+ move.getEndPosition().getColumn();
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            message.setMessage(returnString);
            connections.broadcast(session,message, command.getGameID());



            Gson gson = new Gson();
            String returnGame= gson.toJson(gameDAO.getGame(stringGameId));

            ServerMessage gameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            gameMessage.setGame(returnGame);

            connections.broadcast(null,gameMessage, command.getGameID());



        }
        catch (chess.InvalidMoveException e){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error Invalid move");
            String msg = errorMessage.toString();

            session.getRemote().sendString(msg);

        }

        }

    private static void sendError(Session session, String error) throws IOException {
        ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        errorMessage.setErrorMessage(error);
        String msg = errorMessage.toString();

        session.getRemote().sendString(msg);

    }

    private static String getUserName(Session session, AuthData authData) throws IOException {
        if(authData == null)
        {
            sendError(session,"Error Unauthorized");

        }
        assert authData != null;
        String userName= authData.username();
        return userName;
    }

    private void staleMateHandler(String s, Integer gameID) throws DataAccessException, IOException {
        String stringGameId = String.valueOf(gameID);
        GameData game = gameDAO.getGame(stringGameId);
        gameDAO.updateGame(stringGameId,game.whiteUsername(),game.blackUsername(),game.gameName(),game.game(),true);

        String returnString = "Stalemate\n GameOver!";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);
        connections.broadcast(null,message, gameID);

    }

    private void checkMessage(String userName, Integer gameID) throws IOException {

        String returnString = userName+" is in Check";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);
        connections.broadcast(null,message, gameID);

    }

    private void checkMateHandler(String userName, Integer gameID) throws IOException, DataAccessException {
        String stringGameId = String.valueOf(gameID);
        GameData game = gameDAO.getGame(stringGameId);
        gameDAO.updateGame(stringGameId,game.whiteUsername(),game.blackUsername(),game.gameName(),game.game(),true);

        String returnString = userName+" is in CheckMate\n GameOver!";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);
        connections.broadcast(null,message, gameID);

    }




    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {

        String username = getUserName(session,authDAO.getAuth(command.getAuthToken()));
        String returnString = username+" has left the game.";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);

        String stringGameId = String.valueOf(command.getGameID());
        GameData game = gameDAO.getGame(String.valueOf(stringGameId));
        if (Objects.equals(game.whiteUsername(), username)){
            gameDAO.updateGame(stringGameId,null,game.blackUsername(),game.gameName(),game.game(),game.isOver());
        }
        else if (Objects.equals(game.blackUsername(), username)){
            gameDAO.updateGame(stringGameId,game.whiteUsername(),null,game.gameName(),game.game(),game.isOver());
        }

        connections.remove(session,command.getGameID());
        GameData changed = gameDAO.getGame(String.valueOf(stringGameId));
        connections.broadcast(session,message, command.getGameID());
    }

    private void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        String authToken = command.getAuthToken();
        String stringGameId = String.valueOf(command.getGameID());

        try{
            userService.verify(authToken);


            if (gameDAO.getGame(stringGameId) == null){
                sendError(session,"Error Bad game ID");
                return;
            }

        GameData gameData = gameDAO.getGame(stringGameId);
            String role = role = "spectator";
            if ( command.getRole() != null){
                role = command.getRole();
            }

            String username = command.getUserName();


            connections.add(session,command.getGameID());

        String returnString = username+" has joined the game as "+role ;
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(returnString);
        connections.broadcast(session,message, command.getGameID());



        Gson gson = new Gson();
        String game= gson.toJson(gameData);

        ServerMessage gameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        gameMessage.setGame(game);

        String msg = gameMessage.toString();

        session.getRemote().sendString(msg);


        }
        catch (UnauthorisedException e){
            sendError(session,"Error Unauthorized");

        }
    }


    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}