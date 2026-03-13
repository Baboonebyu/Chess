package client;
import static java.lang.System.out;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
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
    int bCol = 0;
    int bRow = 7;
    String colorState = "White";
    int modCol = 1;
    int modRow = -1;

    public void setUp(){
        if (Objects.equals(color, "Black")){
            bCol = 7;
            bRow = 0;
            modCol = -1;
            modRow = -1;

        }
    }



    public void drawBoard(ChessBoard board){
        this.board = board;
       //top row
        setUp();
        letterLine();


        while (bRow>=0) {

            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(colHeader[bRow]);
            swapState();
            while (bCol < 8) {
                printPiece(bRow, bCol);
                bCol += modCol;
                swapState();
            }
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(colHeader[bRow]);
            bCol = 0;
        finishLine();
        bRow += modRow;

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

    private void printPiece(int row, int col) {

        ChessPiece piece = board.getPiece(new ChessPosition(row + 1, col + 1));
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
