package model;

public class Request {
    String message;


    public static class RegisterRequest extends Request {
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

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class LoginRequest extends Request {
        String username;
        String password;

        public String getUsername(){return username;}
        public String getPassword(){
            return password;
        }

    }

    public static class ClearRequest extends Request{
    }

    public static class CreateGameRequest extends Request{
        String gameName;

        public String getGameName(){
            return gameName;
        }
    }


    public static class LogoutRequest extends Request{
        String authToken;

        public LogoutRequest(String authToken) {
            this.authToken = authToken;
        }

        public String getAuthToken() {
            return authToken;
        }
    }
    public static class ListGamesRequest extends Request{
    }

    public static class JoinGameRequest extends Request {
        String playerColor;
        String gameID;

        public String getGameID() {
            return gameID;
        }
        public String getPlayerColor() {
            return playerColor;
        }
    }
}

