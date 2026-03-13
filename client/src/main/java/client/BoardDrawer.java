package client;
import static java.lang.System.out;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

import static ui.EscapeSequences.*;


public class BoardDrawer {
    String color;
    ChessBoard board;
    public BoardDrawer(String color) {
        this.color = color;
    }
    String[] rowHeader = {" a "," b "," c "," d "," e "," f "," g "," h "};
    String[] colHeader = {" 1 "," 2 "," 3 "," 4 "," 5 "," 6 "," 7 "," 8 "};

    int pRow = 8;
    int pCol = 1;

    int rowCount = 1;
    int colCount =1;
    int header = 8;
    int headerMod = -1;


    String colorState = "White";
    int modCol = 1;
    int modRow = -1;

    public void setUp(){
        if (Objects.equals(color, "Black")){

            modCol = -1;
            modRow = -1;

        }
    }



    public void drawBoard(ChessBoard board){
        this.board = board;
       //top row
        setUp();
        letterLine();


        while (rowCount <= 8) {

            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(" "+header+" ");
            swapState();
            while (colCount <= 8) {
                printPiece();
                colCount +=1;
                pCol += modCol;
                swapState();
            }
            out.print(SET_BG_COLOR_DARK_GREY);
            pCol =1;
            out.print(" "+header+" ");

            header+=headerMod;
            colCount = 1;
        finishLine();
        pRow += modRow;
        rowCount +=1;

        }

        letterLine();

    }

    private void letterLine() {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("   ");
        for (String rowHead: rowHeader){
            out.print(rowHead);
        }
        out.print("   ");
        finishLine();
    }

    private static void finishLine() {
        out.print(SET_BG_COLOR_BLACK);
        out.print('\n');
    }
    private  void swapState(){


        if (Objects.equals(colorState, "White")){
            out.print(SET_BG_COLOR_WHITE);
            colorState = "Black";
        }
        else {
            out.print(SET_BG_COLOR_BLACK);
            colorState = "White";
        }
    }

    private void printPiece() {

        ChessPiece piece = board.getPiece(new ChessPosition(pRow, pCol));
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_BLUE);
            } else {
                out.print(SET_TEXT_COLOR_RED);
            }

            out.print(" ");
            out.print(piece);
            out.print(" ");
            out.print(SET_TEXT_COLOR_WHITE);
        }
        else{
            out.print("   ");
        }
    }

}
