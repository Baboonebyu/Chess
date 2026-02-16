package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;




    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

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
        // Other routes here
    }

}

