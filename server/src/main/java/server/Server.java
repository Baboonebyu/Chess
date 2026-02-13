package server;

import io.javalin.*;
import io.javalin.http.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.


        System.out.println("reg");
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
        // Other routes here
    }

}

