package sharedServerFiles;

import model.Request.*;
import model.Response.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
         serverUrl = url;
    }

    public RegisterResponse registerUser(RegisterRequest request) throws Exception {

        return (RegisterResponse) ClientCommunicator.post(serverUrl,  "/user" ,request,RegisterResponse.class,null);
    }

    public LoginResponse loginUser (LoginRequest request) throws Exception {






        return (LoginResponse) ClientCommunicator.post(serverUrl, "/session",request, LoginResponse.class,null);
    }
    public CreateGameResponse createGame(CreateGameRequest request,String token) throws Exception {
        return (CreateGameResponse) ClientCommunicator.post(serverUrl, "/game",request,CreateGameResponse.class,token);


    }
    public ListGamesResponse listGames(ListGamesRequest request, String token) throws Exception {
        return (ListGamesResponse) ClientCommunicator.get(serverUrl, "/game",request,ListGamesResponse.class,token);
    }
  //  public void spectateGame(String gameID){}
    public LogoutResponse logoutUser(LogoutRequest request, String token) throws Exception {
        return (LogoutResponse) ClientCommunicator.delete(serverUrl,  "/session" ,request,LogoutResponse.class,token);
    }

    public JoinGameResponse joinGame(JoinGameRequest request, String token) throws Exception {
        return (JoinGameResponse) ClientCommunicator.put(serverUrl, "/game",request,JoinGameResponse.class,token);
    }

    public void clearDatabase(ClearRequest request) throws Exception {
        ClientCommunicator.delete(serverUrl,  "/db" ,request, ClearResponse.class,null);
    }


}
