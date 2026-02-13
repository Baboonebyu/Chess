package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class Handler implements io.javalin.http.Handler {


    @Override
    public void handle(@NotNull Context context) throws Exception {

    }

    public Request fromJson(String data, Class<RegistarRequest> requestType){
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

            RegistarRequest request = (RegistarRequest) fromJson("", RegistarRequest.class);


            context.result("Hello New user!");


            //RegisterRequest request = ()
            //fromjson

        }
}


