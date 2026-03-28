package client;


import chess.ChessBoard;
import chess.ChessGame;

import java.util.*;

import com.google.gson.Gson;
import model.GameData;
import model.Request;
import model.Request.*;
import model.Response.*;
import static java.lang.System.out;
import shared.server.files.ServerFacade;
import ui.EscapeSequences;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;


public class ChessClient implements NotificationHandler {
    private final ServerFacade server;
    private final WebSocketFacade ws;

    public ChessClient(String serverUrl) throws Exception {
        server = new ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl, this);
    }
    private boolean loggedIn = false;
    private boolean inGame = false;
    private String currentGameID = null;
    private String authToken = null;
    private String clientName = null;
    private String color = null;
    private ChessGame game = null;
    private boolean spectating = false;




    public void run () {
        BoardDrawer drawer = new BoardDrawer("Black");
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
       // drawer.drawBoard(board);
        loopMain();
    }


    @Override
    public void notify(ServerMessage message)  {


        if (message.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println( SET_TEXT_COLOR_WHITE + message.getMessage());

        } else if (message.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(SET_TEXT_COLOR_RED+ "Error from server");
        } else if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            Gson gson = new Gson();
            game = gson.fromJson(message.getGame(),ChessGame.class);

            BoardDrawer drawer = new BoardDrawer(color);
            drawer.drawBoard(game.getBoard());


        } else{
            System.out.println(message.getServerMessageType());
        }



    }





    private void loopMain(){
        out.print(SET_TEXT_COLOR_GREEN+"Welcome to Chess, Log in or type HELP for more info \n\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")&& !result.equals("QUIT")) {



            out.print(ERASE_SCREEN);
            if (!loggedIn){
                printLoggedOutMenu();}
            else if (spectating) {
                printSpectatingMenu();
            } else if (!inGame){
                printLoggedInMenu();

            } else{
                printGameMenu();
            }




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

    private void printSpectatingMenu() {
        out.print("\nRedraw \n");
        out.print("Highlight <Piece Location> \n");
        out.print("Leave\n");
        out.print("Help\n");
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
    private void printGameMenu(){
        out.print("Redraw \n");
        out.print("Move <Current Location> <New location> <PromotionType>\n");
        out.print("Highlight <Piece Location> \n");
        out.print("Leave\n");
        out.print("Resign\n");
        out.print("Help\n");

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
                case "redraw" -> redraw();
                case "move" -> makeMove(params);
         //       case "highlight" -> highlight();
                case "leave" -> leave();
                case "resign" -> resign();


                default -> help();
            };
        } catch (Exception ex) {
            out.print(SET_TEXT_COLOR_RED);
            return ex.getMessage();
        }
    }

    private String makeMove(String[] params) throws Exception {
        if(!inGame){
            throw new Exception("Invalid Command");
        }
        if (params.length >=2 ) {
            String currentCords = params[0];
            String newCords= params[1];
            String promote = "none";
            if (params.length >2){
              promote = params[2];
            }

            ws.Move(authToken,Integer.valueOf(currentGameID),clientName,currentCords,newCords,promote);




            return "";

        }
        throw new Exception("Invalid format: Expected CurrentPosition NewPosition\n");



    }

    private String resign() throws Exception {
        if(!inGame){
            throw new Exception("Invalid Command");
        }

        Scanner scanner = new Scanner(System.in);
        var result = "";



        out.print("Are you Sure Yes/No\n");
        printPrompt();
        String input = scanner.nextLine();
        String[] tokens = input.toLowerCase().split(" ");
        if (tokens.length == 1){
            if(Objects.equals(tokens[0], "yes")){
                ws.resign(authToken,Integer.valueOf(currentGameID),clientName);
                return "";
               // return "You have resigned\n";
            }
        }
        return "You are still in the game\n";







    }

    private  String leave() throws Exception {
        if(!inGame){
            throw new Exception("Invalid Command");
        }

        ws.Leave(authToken,Integer.valueOf(currentGameID),clientName);

        spectating = false;
        inGame = false;
        return "You have left the game";

    }

    private String redraw() throws Exception {

        if(!inGame){
            throw new Exception("Invalid Command");
        }


        BoardDrawer drawer = new BoardDrawer(color);
        drawer.drawBoard(game.getBoard());
        return "";
    }

    private String help() {
        if (!loggedIn){
           out.print("To login or register please enter the command word \n");
           out.print("Followed by the necessary arguments as found in the <>\n");
           out.print("Example >>> Login Bob password\n");
        } else if (spectating) {
            out.println("Redraw displays the board again");
            out.println("Highlight Shows valid moves. Example A2");
            out.println("Leave makes you leave game");
            out.println("Help displays this message");
        } else if (!inGame){
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
        else{



            out.println("Redraw displays the board again");
            out.println("Move moves a piece. Example Move A2 A3");
            out.println("Highlight Shows valid moves. Example A2");
            out.println("Leave makes you leave game");
            out.println("Resign has you forfeit");
            out.println("Help displays this message");
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

        if(games.isEmpty()){
            return "No games currently";
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
            if (gameID-1 > games.size() || gameID-1 <0){
                throw new Exception("Invalid Game ID \n");
            }
            GameData gameJ = games.get(gameID - 1);
            currentGameID = String.valueOf((gameID));
            if (color.equals("WHITE") && gameJ.whiteUsername() != null){
                throw new Exception("White is already taken. \n");
            }
            if (color.equals("BLACK") && gameJ.blackUsername() != null){
                throw new Exception("BLACK is already taken. \n");
            }

            try { ws.connect(authToken, Integer.valueOf(currentGameID), clientName);}catch (Exception e){
                throw new Exception("Error connecting to the game");
            }


            JoinGameRequest request = new JoinGameRequest();
            request.setPlayerColor(color);
            request.setGameID(gameJ.gameID());
            JoinGameResponse response = server.joinGame(request, authToken);
            this.color = color;
            game = gameJ.game();
            BoardDrawer drawer = new BoardDrawer(color);
 //           drawer.drawBoard(game.getBoard());
            inGame = true;






            return "Joining "+ gameJ.gameName()+ " game.\n";

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
            int gameID;


            try {
                gameID = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new Exception("Invalid Game Id: Expected Number\n");
            }

            ArrayList<GameData> games = getGames();
            if (gameID-1 > games.size()){
                throw new Exception("Invalid Game ID \n");
            }
            currentGameID = String.valueOf((gameID));
            try { ws.connect(authToken, Integer.valueOf(currentGameID), clientName);}catch (Exception e){
                throw new Exception("Error connecting to the game");
            }

            GameData game = games.get(gameID - 1);
            currentGameID = String.valueOf((gameID));
            ChessGame currentGame = game.game();
            BoardDrawer drawer = new BoardDrawer("WHITE");
            drawer.drawBoard(currentGame.getBoard());

            spectating = true;


            return "Spectating "+game.gameName()+" game.";

        }
        throw new Exception("Invalid format: Expected GameID\n");
    }
    private String logout() throws Exception {
        Request.LogoutRequest request = new Request.LogoutRequest(authToken);


        LogoutResponse response = server.logoutUser(request,authToken);
        if (response.getMessage() != null){
            throw new Exception(response.getMessage());
        }


        spectating = false;
        clientName = null;
        authToken = null;
        loggedIn = false;
        currentGameID = null;
        return "Goodbye! \n";
    }

}


