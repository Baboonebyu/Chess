package server;

import io.javalin.*;
import websocket.WebSocketHandler;

public class Server {

    private final Javalin javalin;
    private final WebSocketHandler webSocketHandler;


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        webSocketHandler = new WebSocketHandler();
        // Register your endpoints and exception handlers here.


        createHandlers(javalin);


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


    private void createHandlers(
            Javalin javalinServer) {
        javalinServer.get("/hello",
                new HelloBYUHandler());
        javalinServer.post("/user",
                new RegistarHandler());
        javalinServer.delete("/db",
                new DeleteHandler());
        javalinServer.post("/session",
                new LoginHandler());
        javalinServer.delete("/session",
                new LogoutHandler());
        javalinServer.get("/game",
                new ListGameHandler());
        javalinServer.post("/game",
                new CreateGameHandler());
        javalinServer.put("/game",
                new JoinGameHandler());
        javalinServer.ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });
        // Other routes here
    }

}

