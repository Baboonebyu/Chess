package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String msg) {
                    ServerMessage message = new Gson().fromJson(msg, ServerMessage.class);
                    notificationHandler.notify(message);



                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception( ex.getMessage());
        }

    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        int count =0;
        count=count+1;


    }
    public void connect(String authToken, Integer gameID,String userName) throws Exception {

        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameID,userName);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new Exception();
        }


    }

    public void Leave(String authToken, Integer gameID,String userName) throws Exception {

        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE,authToken,gameID,userName);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new Exception();
        }
    }

    public void resign(String authToken, Integer gameID, String userName) throws Exception {

        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN,authToken,gameID,userName);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new Exception();
        }


    }

    public void Move(String authToken, Integer gameID, String userName, String currentCords, String newCords,String promote) throws Exception {

        //convert cords to chess move

        Map<String, Integer> valuemap = Map.ofEntries(
                Map.entry("a",1),
                Map.entry("b",2),
                Map.entry("c",3),
                Map.entry("d",4),
                Map.entry("e",5),
                Map.entry("f",6),
                Map.entry("g",7),
                Map.entry("h",8)


        );
        Map<String, ChessPiece.PieceType> promotemap = Map.ofEntries(
                Map.entry("queen",ChessPiece.PieceType.QUEEN),
                Map.entry("rook",ChessPiece.PieceType.ROOK),
                Map.entry("bishop",ChessPiece.PieceType.BISHOP),
                Map.entry("knight",ChessPiece.PieceType.KNIGHT)



        );


        currentCords = currentCords.toLowerCase(Locale.ROOT);
        newCords= newCords.toLowerCase(Locale.ROOT);
        if (promote != null) {
            promote = promote.toLowerCase(Locale.ROOT);
        }

        String[] currentSplit = currentCords.split("");

        String[] newSplit = newCords.split("");
        ChessPosition currentPos = new ChessPosition( Integer.parseInt(currentSplit[1]),valuemap.get((currentSplit[0])));
        ChessPosition newPos = new ChessPosition(Integer.parseInt(newSplit[1]),valuemap.get((newSplit[0])));
        ChessPiece.PieceType promoteType = promotemap.get(promote);


        ChessMove move = new ChessMove(currentPos, newPos, promoteType);



        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,authToken,gameID,userName);
            command.setMove(move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException e){
            throw new Exception();
        }

    }
}
