package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveCalculator {

    //Todo
    //Temp need to return list of moves not an empty one
    // I believe that this is where each move will be coded.
    // I might make functions to call for each type so the code is easier to read
    //Todo make a inbounds/outofbounds function
    public MoveCalculator(ChessBoard board, ChessPosition myPosition) {
    }

    public static Collection<ChessMove> pieceCalc(ChessBoard board, ChessPosition myPosition) {


    ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
        //Todo don't hard code this in
            return bishopCalc(myPosition);

    }
        return List.of();
    }
    private static boolean inbounds(int newRow, int newCol){
        if (newRow < 1 || newRow > 8)
            return false;
        else return newCol >= 1 && newCol <= 8;

    }


    private static Collection<ChessMove> bishopCalc( ChessPosition myPosition){

        List<ChessMove> BishopMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        //left up
        boolean inbound = true;
        int rowN = row;
        int colN = col;
        while (inbound) {
            rowN = rowN-1;
            colN = colN+1;
            if (inbounds(rowN,colN)) {
                System.out.printf("Inbounds %d,%d ", rowN, colN);
                BishopMoves.add(new ChessMove( new ChessPosition(row,col),new ChessPosition(rowN,colN), null));
            }
            else
            {System.out.printf("Out of bounds %d,%d ",rowN,colN);
                inbound = false;}}
        //upright
        inbound = true;
        rowN = row;
        colN = col;
        while (inbound) {
            rowN = rowN+1;
            colN = colN+1;
            if (inbounds(rowN,colN)) {
                System.out.printf("Inbounds %d,%d ", rowN, colN);
                BishopMoves.add(new ChessMove( new ChessPosition(row,col),new ChessPosition(rowN,colN), null));
            }
            else
            {System.out.printf("Out of bounds %d,%d ",rowN,colN);
                inbound = false;}}
        //downright
        inbound = true;
        rowN = row;
        colN = col;
        while (inbound) {
            rowN = rowN+1;
            colN = colN-1;
            if (inbounds(rowN,colN)) {
                System.out.printf("Inbounds %d,%d ", rowN, colN);
                BishopMoves.add(new ChessMove( new ChessPosition(row,col),new ChessPosition(rowN,colN), null));
            }
            else
            {System.out.printf("Out of bounds %d,%d ",rowN,colN);
                inbound = false;}}
        //downLeft
        inbound = true;
        rowN = row;
        colN = col;
        while (inbound) {
            rowN = rowN-1;
            colN = colN-1;
            if (inbounds(rowN,colN)) {
                System.out.printf("Inbounds %d,%d ", rowN, colN);
                BishopMoves.add(new ChessMove( new ChessPosition(row,col),new ChessPosition(rowN,colN), null));
            }
            else
            {System.out.printf("Out of bounds %d,%d ",rowN,colN);
                inbound = false;}}




        return BishopMoves;
    }

}
