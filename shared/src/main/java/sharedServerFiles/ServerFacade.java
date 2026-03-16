package sharedServerFiles;

import model.Request.*;
import model.Response.*;

import java.net.http.HttpClient;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResponse registerUser(RegisterRequest request){
        RegisterResponse response = new RegisterResponse();
        return response;
    }

    public LoginResponse loginUser (LoginRequest request){

        LoginResponse response = new LoginResponse();




        return response;
    }
    public void createGame(String gameName){}
    public void listGames(){}
    public void spectateGame(String gameID){}
    public void logoutUser(){}





}
