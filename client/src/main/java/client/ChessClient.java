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
    private boolean logedIn = false;
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
            if (!logedIn){printMenu1();}
            else if (logedIn && !inGame){
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
        logedIn = false;
        inGame = false;
    }

    private void logedInLoop(){





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

        out.print("\nCREATE <Name> \n");
        out.print("LIST \n");
        out.print("JOIN <ID> [White|Black]\n");
        out.print("OBSERVE <ID>\n");
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
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        if (!logedIn){
           out.print("To login or register please enter the command word \n");
           out.print("Followed by the necessary arguments as found in the <>\n");
           out.print("Example >>> Login Bob password\n");
        }
        else if (!inGame){
            out.print("loged in help goes here\n");
        }


        return "";
    }

    private String login(String[] params) {
        logedIn = true;
        return "hi " +params[0];






    }


    private String register(String[] params){
        return "hi register";
    }
}


