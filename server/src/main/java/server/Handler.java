package server;

import Model.Request;
import Model.Request.*;
import Model.Request.RegisterRequest;
import Model.Response;
import Model.Response.*;
import Service.ClearService;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.http.Context;

import java.util.Objects;

public abstract class Handler implements io.javalin.http.Handler {

    public final static UserDAO userDAO = new UserMemoryDataAccess();
    public static AuthDAO authDAO = new AuthMemoryDataAccess();
    UserService userService = new UserService();
    ClearService clearService = new ClearService();
    public <T> Request fromJson(String data, Class<T> requestType){
        Gson gson = new Gson();
        return (Request) gson.fromJson(data, requestType);
    }
    public Object toJson(Response response){
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
            if (response!= null && Objects.equals(response.getMessage(), "Error Username Already taken")){
                context.status(403);
                //todo
                //make this better
            } else if (response!= null && Objects.equals(response.getMessage(), "Error Bad Request")) {
                context.status(400);
            }


            Object jsonResponse = toJson(response);
            context.result(jsonResponse.toString());




        }
}

class DeleteHandler extends Handler{
    public void handle(Context context) throws DataAccessException {

        ClearRequest request = new ClearRequest();
        Response response = clearService.Clear(request);

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());
    }
}

class LoginHandler extends Handler{
    public void handle(Context context) throws DataAccessException{
        context.contentType("application/json");

       LoginRequest request = (LoginRequest) fromJson(context.body(), LoginRequest.class);
        LoginResponse response = userService.login(request);

        if (response!= null && Objects.equals(response.getMessage(), "Error Unauthorised")){
            context.status(401);
            //todo
            //make this better
        } else if (response!= null && Objects.equals(response.getMessage(), "Error Bad Request")) {
            context.status(400);
        }

        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());
    }
}

class LogoutHandler extends Handler{
    public void handle(Context context) throws  DataAccessException{
        String authToken = context.header("authorization");
        System.out.println(authToken);
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResponse response =userService.logout(request);

        if(Objects.equals(response.getMessage(), "Error: unauthorized")){
            context.status(401);
        }
        Object jsonResponse = toJson(response);
        context.result(jsonResponse.toString());

    }
}







