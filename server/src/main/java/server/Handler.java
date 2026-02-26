package server;

import model.Request;
import model.Request.*;
import model.Request.RegisterRequest;
import model.Response;
import model.Response.*;
import service.*;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.http.Context;

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

        RegisterResponse response = new RegisterResponse();
        try{response = userService.register(request);
        }
        catch (BadRequestException r){
            response.setMessage(r.getMessage());
            context.status(400);
        }
        catch (AlreadyTakenException r){
            response.setMessage(r.getMessage());
            context.status(403);
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
        LoginResponse response = new LoginResponse();



        try{response = userService.login(request);}
        catch (UnauthorisedException r){
            response.setMessage(r.getMessage());
            context.status(401);
        }
        catch (BadRequestException r){

            response.setMessage(r.getMessage());
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

        boolean Success = false;
        try{
            userService.verify(authToken);
            Success = true;
        }
        catch (UnauthorisedException r){
            context.status(401);
            response.setMessage(r.getMessage());

        }
        if(Success){ LogoutRequest request = new LogoutRequest(authToken);
            response = userService.logout(request);}


        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());

    }
}

class ListGameHandler extends Handler {
    public void handle(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        ListGamesResponse response = new ListGamesResponse();


        boolean Success = false;
        try{
            userService.verify(authToken);
            Success = true;
        }
        catch (UnauthorisedException r){
            context.status(401);
            response.setMessage(r.getMessage());

        }
        if(Success){ ListGamesRequest request = new ListGamesRequest();
            response = gameService.list(request);}



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
        boolean Success = false;
        try{
            userService.verify(authToken);
            Success = true;
        }
        catch (UnauthorisedException r){
            context.status(401);
            response.setMessage(r.getMessage());

        }
        if(Success){
            CreateGameRequest request = (CreateGameRequest) fromJson(context.body(), CreateGameRequest.class);

            try{response = gameService.create(request);}
            catch (BadRequestException r){
                context.status(400);
                response.setMessage(r.getMessage());
            }
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

        boolean Success = false;
        try{
            userService.verify(authToken);
            Success = true;
        }
        catch (UnauthorisedException r){
            context.status(401);
            response.setMessage(r.getMessage());

        }
        if (Success){
            JoinGameRequest request = (JoinGameRequest) fromJson(context.body(), JoinGameRequest.class);
            String userName = authDAO.getAuth(authToken).username();

            try{
            response = gameService.join(request, userName);}
            catch (BadRequestException r){
                context.status(400);
                response.setMessage(r.getMessage());
            }
            catch (AlreadyTakenException r){
                context.status(403);
                response.setMessage(r.getMessage());

            }
        }

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());


    }
}






