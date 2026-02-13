package server;

public class Request {
    String message;

}

class RegisterRequest extends Request {
    String username;
    String password;
    String email;

}

class LoginRequest extends Request {
    String username;
    String password;

}