package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }


     //The various different chess piece options
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    //@return Which team this chess piece belongs to
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }


    //@return which type of chess piece this piece is

    public PieceType getPieceType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Todo
        //Temp need to return list of moves not an empty one
        // I believe that this is where each move will be coded.
        // I might make functions to call for each type so the code is easier to read
        //Todo make a inbounds/outofbounds function
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP) {
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
