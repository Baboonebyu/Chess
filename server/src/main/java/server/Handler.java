package server;

import com.google.gson.Gson;
import io.javalin.http.Context;

import org.jetbrains.annotations.NotNull;

public abstract class Handler implements io.javalin.http.Handler {


    public Request fromJson(String data, Class<RegisterRequest> requestType){
        Gson gson = new Gson();
        return gson.fromJson(data, requestType);
    }
}


class HelloBYUHandler extends Handler {
    public void handle(Context context) {
        context.result("Hello BYU!");
    }
}

    class RegistarHandler extends Handler {
        public void handle(Context context) {
            context.contentType("application/json");

            RegisterRequest request = (RegisterRequest) fromJson(context.body(), RegisterRequest.class);
            System.out.println(request);

            context.result("Hello New user!");


            //RegisterRequest request = ()
            //fromjson

        }
}


