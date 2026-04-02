package client;
import static java.lang.System.out;

import chess.*;

import java.util.ArrayList;
import java.util.Objects;

import static ui.EscapeSequences.*;


public class BoardDrawer {
    String color;
    ChessBoard board;
    ChessPosition current = new ChessPosition(100,100);
    ArrayList<ChessPosition> valid = new ArrayList<ChessPosition>();

    public BoardDrawer(String color) {
        this.color = color;
    }
    String[] rowHeader = {" a "," b "," c "," d "," e "," f "," g "," h "};

    int pRow = 8;
    int pCol = 1;

    int rowCount = 1;
    int colCount =1;
    String[] header ={"9","8", "7", "6", "5", "4", "3", "2", "1"};
    int headerMod = -1;


    String colorState = "White";
    int modCol = 1;
    int modRow = -1;
    int cInit =1;
    public void setUp(){
        if (Objects.equals(color, "BLACK")){
            rowHeader = new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
            header = new String[]{"0","1", "2", "3", "4","5","6","7","8"};
            pRow = 1;
            pCol = 8;
            modCol = -1;
            modRow = 1;
            cInit = 8;

        }
    }



    public void drawBoard(ChessBoard board, ArrayList<ChessMove> highlight){
        this.board = board;
       //top row
        out.print("\n");
        out.print(ERASE_SCREEN);
        setUp();
        letterLine();

        if (highlight != null){
            current = highlight.getFirst().getStartPosition();
            for (ChessMove move: highlight){
                valid.add(move.getEndPosition());
            }

        }



        while (rowCount <= 8) {

            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(" "+ header[rowCount] +" ");
            swapState();
            while (colCount <= 8) {
                if (valid.contains(new ChessPosition(pRow,pCol))){
                    if(Objects.equals(colorState, "Black")) {
                        out.print(SET_BG_COLOR_GREEN);
                    }
                    else {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                }
                if(Objects.equals(current, new ChessPosition(pRow, pCol))){
                    out.print(SET_BG_COLOR_YELLOW);
                }
                printPiece();
                colCount +=1;
                pCol += modCol;
                swapState();
            }
            out.print(SET_BG_COLOR_DARK_GREY);
            pCol = cInit ;
            out.print(" "+header[rowCount]+" ");

          //  header+=headerMod;
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
        out.print(RESET_BG_COLOR);
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
