package server;

import model.Request;
import model.Request.*;
import model.Request.RegisterRequest;
import model.Response;
import model.Response.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.http.Context;

import java.util.Objects;

public abstract class Handler implements io.javalin.http.Handler {

    public static UserDAO userDAO = new UserMemoryDataAccess();
    public static AuthDAO authDAO = new AuthMemoryDataAccess();
    public static GameDAO gameDAO = new GameMemoryDataAccess();
    UserService userService = new UserService();
    ClearService clearService = new ClearService();
    GameService gameService = new GameService();

    public <T> Request fromJson(String data, Class<T> requestType) {
        Gson gson = new Gson();
        return (Request) gson.fromJson(data, requestType);
    }

    public Object toJson(Response response) {
        Gson gson = new Gson();
        return gson.toJson(response);
    }

}


class HelloBYUHandler extends Handler {
    public void handle(Context context) {
        context.result("Hello BYU!");
    }
}

class RegistarHandler extends Handler {
    public void handle(Context context) throws DataAccessException {
        context.contentType("application/json");

        RegisterRequest request = (RegisterRequest) fromJson(context.body(), RegisterRequest.class);
        // System.out.println(request);


        Response response = userService.register(request);
        if (response != null && Objects.equals(response.getMessage(), "Error Username Already taken")) {
            context.status(403);
            //todo
            //make this better
        } else if (response != null && Objects.equals(response.getMessage(), "Error Bad Request")) {
            context.status(400);
        }


        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());


    }
}

class DeleteHandler extends Handler {
    public void handle(Context context) throws DataAccessException {

        ClearRequest request = new ClearRequest();
        Response response = clearService.clear(request);

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());
    }
}

class LoginHandler extends Handler {
    public void handle(Context context) throws DataAccessException {
        context.contentType("application/json");

        LoginRequest request = (LoginRequest) fromJson(context.body(), LoginRequest.class);
        LoginResponse response = userService.login(request);

        if (response != null && Objects.equals(response.getMessage(), "Error Unauthorised")) {
            context.status(401);
            //todo
            //make this better
        } else if (response != null && Objects.equals(response.getMessage(), "Error Bad Request")) {
            context.status(400);
        }

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());
    }
}

class LogoutHandler extends Handler {
    public void handle(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        LogoutResponse response = new LogoutResponse();


        if (userService.verify(authToken)) {
            LogoutRequest request = new LogoutRequest(authToken);
            response = userService.logout(request);
        } else {
            context.status(401);
            response.setMessage("Error: unauthorized");
        }

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());

    }
}

class ListGameHandler extends Handler {
    public void handle(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        ListGamesResponse response = new ListGamesResponse();


        if (userService.verify(authToken)) {
            ListGamesRequest request = new ListGamesRequest();
            response = gameService.list(request);
        } else {
            context.status(401);
            response.setMessage("Error: unauthorized");
        }

        Object jsonResponse = toJson(response);
        System.out.println(jsonResponse);
        context.result(jsonResponse.toString());


    }

}

class CreateGameHandler extends Handler {
    public void handle(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        context.contentType("application/json");
        CreateGameResponse response = new CreateGameResponse();
        if (userService.verify(authToken)) {
            CreateGameRequest request = (CreateGameRequest) fromJson(context.body(), CreateGameRequest.class);

            response = gameService.create(request);
            if (response.getMessage() != null && Objects.equals(response.getMessage(), "Error: Bad Request")) {
                context.status(400);
            }
        } else {
            context.status(401);
            response.setMessage("Error: unauthorized");
        }

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());

    }
}

class JoinGameHandler extends Handler {
    public void handle(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        context.contentType("application/json");


        JoinGameResponse response = new JoinGameResponse();
        if (userService.verify(authToken)) {
            JoinGameRequest request = (JoinGameRequest) fromJson(context.body(), JoinGameRequest.class);
            String userName = authDAO.getAuth(authToken).username();
            response = gameService.join(request, userName);
            if (response != null && Objects.equals(response.getMessage(), "Error: Bad Request")) {
                context.status(400);
            } else if (response != null && Objects.equals(response.getMessage(), "Error: already taken")) {
                context.status(403);
            }
        } else {
            context.status(401);
            response.setMessage("Error: unauthorized");
        }

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());


    }
}






