package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveCalculator {
    public MoveCalculator(ChessBoard board, ChessPosition myPosition) {
    }

    public static Collection<ChessMove> piececalc(ChessBoard board, ChessPosition myPosition) {


    ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
        //Todo don't hard code this in
        //equals method????
        List<ChessMove> BishopMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int drow= 8-row;
        int dcol = 8-col;
        BishopMoves.add(new ChessMove( new ChessPosition(5,4),new ChessPosition(1,8), null));
        BishopMoves.add(new ChessMove( new ChessPosition(5,4),new ChessPosition(2,1), null));

        return BishopMoves;
    }
        return List.of();
    }
}
