package client;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
}
