package server;

import Model.Request;
import Model.Request.RegisterRequest;
import Model.Response;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;

public abstract class Handler implements io.javalin.http.Handler {


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
            System.out.println(request);

            UserService service = new UserService();
            service.register(request);

            Response response = new Response();


            Object jsonResponse = toJson(response);
            context.result(jsonResponse.toString());




        }
}





