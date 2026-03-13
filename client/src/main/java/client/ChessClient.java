package client;


import chess.ChessBoard;
import chess.ChessGame;

public class ChessClient {

    public ChessClient() {
    }


    public void run () {
        BoardDrawer drawer = new BoardDrawer("White");
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
        drawer.drawBoard(board);
    }
}
