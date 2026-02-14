package Model;

public class Response {
    String message;



    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static class RegisterResponse extends Response{
        String authtoken;
        String username;
        //todo i don't think this should be static

    }
    public class LoginResponse extends Response {
        String authtoken;
        String username;
    }


}


