package Model;

public class Response {
    String message;


    public class RegisterResponse extends Response{
        String authtoken;
        String username;
    }
    public class LoginResponse extends Response {
        String authtoken;
        String username;
    }


}


