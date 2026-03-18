package sharedServerFiles;

import model.Request.*;
import model.Response;
import model.Response.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
         serverUrl = url;
    }

    public Response registerUser(RegisterRequest request) throws Exception {

        return ClientCommunicator.post(serverUrl,  "/user" ,request,RegisterResponse.class);
    }

    public LoginResponse loginUser (LoginRequest request) throws Exception {






        return (LoginResponse) ClientCommunicator.post(serverUrl, "/session",request, LoginResponse.class);
    }
    public void createGame(String gameName){}
    public void listGames(){}
    public void spectateGame(String gameID){}
    public void logoutUser(){}





}
