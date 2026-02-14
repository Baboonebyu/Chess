package Model;

public class Request {
    String message;


    public class RegisterRequest extends Request {
        String username;
        String password;
        String email;


        public String getUsername(){
            return username;
        }
        public String getPassword(){
            return password;
        }
        public String getEmail(){
            return email;
        }


    }

    public class LoginRequest extends Request {
        String username;
        String password;

    }

}

