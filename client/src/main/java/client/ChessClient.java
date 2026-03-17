package client;


import chess.ChessBoard;
import chess.ChessGame;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

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

        out.print("\nREGISTER <Username> <Password> <Email> \n");
        out.print("LOGIN <Username> <Password> \n");
        out.print("QUIT \n");
        out.print("HELP \n");

    }

    private void printLoggedInMenu(){

        out.print("\nCREATE <GameName> \n");
        out.print("LIST \n");
        out.print("JOIN <ID> [White|Black]\n");
        out.print("Spectate <ID>\n");
        out.print("LOGOUT\n");
        out.print("quit\n");
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

            loggedIn = true;
            return "hi " + params[0];

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

            loggedIn = true;
            return "hi " + params[0];

        }
        throw new Exception("Invalid format: Expected username password email\n");


    }

    private String create(String[] params) throws Exception {
        if (params.length == 1) {
            String gameName = params[0];

            return "hi create";

        }
        throw new Exception("Invalid format: Expected GameName\n");

    }
    private String list(){
        return "hi list";
    }
    private String join(String[] params) throws Exception {
        if (params.length == 2) {
            String gameID = params[0];
            String color = params[1];
            color = color.toLowerCase(Locale.ROOT);
            if (!color.equals("white") && !color.equals("black"))
            {
                throw new Exception("Invalid color: Expected White or Black\n");
            }
            return "hi join";

        }
        throw new Exception("Invalid format: Expected GameID Color\n");
    }
    private String spectate(String[] params) throws Exception {
        if (params.length == 1) {
            String gameID = params[0];

            return "hi spectate";

        }
        throw new Exception("Invalid format: Expected GameID\n");
    }
    private String logout(){
        loggedIn = false;
        return "hi logout";
    }
}


