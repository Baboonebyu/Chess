package Model;

public class Request {
    String message;


    public class RegisterRequest extends Request {
        String username;
        String password;
        String email;

    }

    public class LoginRequest extends Request {
        String username;
        String password;

    }

}

