package client;


import chess.ChessBoard;
import chess.ChessGame;

import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.out;

import static ui.EscapeSequences.*;


public class ChessClient {

    public ChessClient() {
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
            if (!loggedIn){printMenu1();}
            else if (loggedIn && !inGame){
                printMenu2();
            }


            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                out.print(result);
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
    private void printMenu1(){

        out.print("\nREGISTER <Username> <Password> <Email> \n");
        out.print("LOGIN <Username> <Password> \n");
        out.print("QUIT \n");
        out.print("HELP \n");

    }

    private void printMenu2(){

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
                case "list" -> list(params);
                case "join" -> join(params);
                case "spectate" -> spectate(params);
                case "logout" -> logout(params);


                default -> help();
            };
        } catch (Exception ex) {
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


            loggedIn = true;
            return "hi " + params[0];

        }
        throw new Exception("Invalid format: Expected username password\n");




    }


    private String register(String[] params) throws Exception {
        if (params.length == 3) {


            loggedIn = true;
            return "hi " + params[0];

        }
        throw new Exception("Invalid format: Expected username password email\n");


    }

    private String create(String[] params) throws Exception {
        if (params.length == 1) {


            return "hi create";

        }
        throw new Exception("Invalid format: Expected GameName\n");

    }
    private String list(String[] params){
        return "hi list";
    }
    private String join(String[] params){
        return "hi join";
    }
    private String spectate(String[] params){
        return "hi spectate";
    }
    private String logout(String[] params){
        loggedIn = false;
        return "hi logout";
    }
}


