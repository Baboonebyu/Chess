package Model;

import java.util.ArrayList;

public class Response {
    String message;



    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static class RegisterResponse extends Response{
        String username;
        String authToken;

        public void setAuthToken(String authToken){
            this.authToken = authToken;
        }
        public void setUsername(String username){
            this.username = username;
        }


    }
    public static class LoginResponse extends Response {
        String authToken;
        String username;
        public void setAuthToken(String authToken){
            this.authToken = authToken;
        }
        public void setUsername(String username){
            this.username = username;
        }
    }

    public static class ClearResponse extends Response{}
    public static class  LogoutResponse extends Response{}
    public static class ListGamesResponse extends Response{
        ArrayList<GameData> games;


        public void setGames(ArrayList<GameData> games) {
            this.games = games;
        }
    }
    public static class CreateGameResponse extends Response{
        String gameID;
        public void setGameID(String gameID){this.gameID = gameID;}
    }

    public static class JoinGameResponse extends Response{
    }
}


