package client;


import chess.ChessBoard;
import chess.ChessGame;

import java.util.*;

import model.GameData;
import model.Request;
import model.Request.*;
import model.Response.*;
import static java.lang.System.out;
import sharedServerFiles.ServerFacade;
import static ui.EscapeSequences.*;


public class ChessClient {
    private final ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }
    private boolean loggedIn = false;
    private boolean inGame = false;
    private String currentGameID = null;
    private String authToken = null;
    private String clientName = null;
    private String color = null;



    public void run () {
        BoardDrawer drawer = new BoardDrawer("Black");
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
       // drawer.drawBoard(board);
        loopMain();
    }




    private void loopMain(){
        out.print(SET_TEXT_COLOR_GREEN+"Welcome to Chess, Log in or type HELP for more info \n\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")&& !result.equals("QUIT")) {



            out.print(ERASE_SCREEN);
            if (!loggedIn){
                printLoggedOutMenu();}
            else if (!inGame){
                printLoggedInMenu();
            }

            //add menu3


            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                out.print(result);
                out.print(SET_TEXT_COLOR_GREEN);
            } catch (Throwable e) {
                var msg = e.toString();

                out.print(msg);

            }
        }
        loggedIn = false;
        inGame = false;
    }


    private void printPrompt() {
        out.print(SET_TEXT_COLOR_WHITE + ">>> " + SET_TEXT_COLOR_GREEN);
    }
    private void printLoggedOutMenu(){

        out.print("REGISTER <Username> <Password> <Email> \n");
        out.print("LOGIN <Username> <Password> \n");
        out.print("QUIT \n");
        out.print("HELP \n");

    }

    private void printLoggedInMenu(){

        out.print("\nCREATE <GameName> \n");
        out.print("LIST \n");
        out.print("JOIN <ID> [White|Black]\n");
        out.print("SPECTATE <ID>\n");
        out.print("LOGOUT\n");
        out.print("QUIT\n");
        out.print("HELP \n");

    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case  "quit" -> "quit";
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "spectate" -> spectate(params);
                case "logout" -> logout();


                default -> help();
            };
        } catch (Exception ex) {
            out.print(SET_TEXT_COLOR_RED);
            return ex.getMessage();
        }
    }

    private String help() {
        if (!loggedIn){
           out.print("To login or register please enter the command word \n");
           out.print("Followed by the necessary arguments as found in the <>\n");
           out.print("Example >>> Login Bob password\n");
        }
        else if (!inGame){
            out.print("Please enter a command word \n");
            out.print("Followed by the necessary arguments as found in the <>\n");
            out.print("Create - Makes a new game\n");
            out.println("List - Lists current games");
            out.println("Join - Joins the game with the ID, Specify color as White or Black");
            out.println("Spectate - Watch the game at the listed ID");
            out.println("Logout - Logout and return to login menu");
            out.println("Quit - Exits the program");
            out.println("Display this message");
        }


        return "";
    }

    private String login(String[] params) throws Exception {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            Request.LoginRequest request = new Request.LoginRequest();
            request.setUsername(username);
            request.setPassword(password);

            LoginResponse response = server.loginUser(request);


            clientName = response.getUsername();
            authToken = response.getAuthToken();
            loggedIn = true;
            return "Welcome back  " + clientName +"\n";

        }
        throw new Exception("Invalid format: Expected username password\n");




    }


    private String register(String[] params) throws Exception {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];

            RegisterRequest request = new RegisterRequest();
            request.setUsername(username);
            request.setPassword(password);
            request.setEmail(email);

            RegisterResponse response = server.registerUser(request);

            if (response.getMessage() != null){
                throw new Exception(response.getMessage());
            }

            clientName = response.getUsername();
            authToken = response.getAuthToken();
            loggedIn = true;
            return "Welcome " + clientName+ "\n";

        }
        throw new Exception("Invalid format: Expected username password email\n");


    }

    private String create(String[] params) throws Exception {
        if (params.length >= 1) {
            String gameName = String.join(" ",params);

            CreateGameRequest request = new CreateGameRequest();
            request.setGameName(gameName);
            CreateGameResponse response = server.createGame(request,authToken);

            if (response.getMessage() != null){
                throw new Exception(response.getMessage());
            }


            return "Your game was created!\n";

        }
        throw new Exception("Invalid format: Expected GameName\n");

    }
    private String list() throws Exception {

        ArrayList<GameData> games = getGames();
        ArrayList<String> gameNames = new ArrayList<>();

        for(GameData game :games){
            gameNames.add(game.gameName());
        }

        StringBuilder sb = new StringBuilder();
        int counter = 1;

        for (GameData game: games){
            sb.append(counter);
            counter+=1;
            sb.append(": ");
            sb.append(game.gameName());
            sb.append(" White player: ");
            if(game.whiteUsername() == null){
                sb.append("Open");
            }
            else{
                sb.append(game.whiteUsername());
            }
            sb.append(", Black player: ");
            if(game.blackUsername() == null){
                sb.append("Open");
            }
            else{
                sb.append(game.blackUsername());
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    private String join(String[] params) throws Exception {
        if (params.length == 2) {

            int gameID;
            try {
                gameID = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new Exception("Invalid Game Id: Expected Number\n");
            }


            String color = params[1];
            color = color.toUpperCase(Locale.ROOT);
            if (!color.equals("WHITE") && !color.equals("BLACK")) {
                throw new Exception("Invalid color: Expected White or Black\n");
            }

            ArrayList<GameData> games = getGames();
            if (gameID-1 > games.size()){
                throw new Exception("Invalid Game ID \n");
            }
            GameData game = games.get(gameID - 1);
            currentGameID = String.valueOf((gameID - 1));
            if (color.equals("WHITE") && game.whiteUsername() != null){
                throw new Exception("White is already taken. \n");
            }
            if (color.equals("BLACK") && game.blackUsername() != null){
                throw new Exception("BLACK is already taken. \n");
            }

            JoinGameRequest request = new JoinGameRequest();
            request.setPlayerColor(color);
            request.setGameID(game.gameID());
            JoinGameResponse response = server.joinGame(request, authToken);
            this.color = color;
            ChessGame currentGame = game.game();
            BoardDrawer drawer = new BoardDrawer(color);
            drawer.drawBoard(currentGame.getBoard());


            return "hi join";

        }
        throw new Exception("Invalid format: Expected GameID Color\n");
    }

    private  ArrayList<GameData> getGames() throws Exception {
        ListGamesRequest request = new ListGamesRequest();
        ListGamesResponse response = server.listGames(request,authToken);
        return (response.getGames());
    }

    private String spectate(String[] params) throws Exception {
        if (params.length == 1) {
            String gameID = params[0];

            return "hi spectate";

        }
        throw new Exception("Invalid format: Expected GameID\n");
    }
    private String logout() throws Exception {
        Request.LogoutRequest request = new Request.LogoutRequest(authToken);


        LogoutResponse response = server.logoutUser(request,authToken);
        if (response.getMessage() != null){
            throw new Exception(response.getMessage());
        }



        clientName = null;
        authToken = null;
        loggedIn = false;
        currentGameID = null;
        return "Goodbye! \n";
    }
}


