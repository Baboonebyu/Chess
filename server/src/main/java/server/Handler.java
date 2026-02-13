package server;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class Handler implements io.javalin.http.Handler {


    @Override
    public void handle(@NotNull Context context) throws Exception {

    }
}


class HelloBYUHandler extends Handler {
    public void handle(Context context) {
        context.result("Hello BYU!");
    }
}

    class RegistarHandler extends Handler {
        public void handle(Context context) {
            context.result("Hello New user!");
            //fromjson

        }
}




