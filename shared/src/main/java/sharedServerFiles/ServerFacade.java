package sharedServerFiles;

import model.Request.*;
import model.Response.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
         serverUrl = url;
    }

    public RegisterResponse registerUser(RegisterRequest request) throws Exception {

        return (RegisterResponse) ClientCommunicator.post(serverUrl,  "/user" ,request,RegisterResponse.class);
    }

    public LoginResponse loginUser (LoginRequest request) throws Exception {






        return (LoginResponse) ClientCommunicator.post(serverUrl, "/session",request, LoginResponse.class);
    }
    public void createGame(String gameName){}
    public void listGames(){}
    public void spectateGame(String gameID){}
    public LogoutResponse logoutUser(LogoutRequest request) throws Exception {
        return (LogoutResponse) ClientCommunicator.delete(serverUrl,  "/session" ,request,LogoutResponse.class);
    }





}
