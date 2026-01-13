package chess;




import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveCalculator {
    private List<ChessMove> movesList = new ArrayList<>();
    //Todo
    //Temp need to return list of moves not an empty one
    // I believe that this is where each move will be coded.
    // I might make functions to call for each type so the code is easier to read



    public Collection<ChessMove> pieceCalc(ChessBoard board, ChessPosition myPosition) {


        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return bishopCalc(myPosition,board);

        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return rookCalc(myPosition,board);
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return queenCalc(myPosition,board);
        }
        return List.of();
    }

    private  boolean inbounds(int newRow, int newCol) {
        if (newRow < 1 || newRow > 8)
            return false;
        else return newCol >= 1 && newCol <= 8;

    }

    private  boolean validMoveCheck(int row, int col, int rowN, int colN, ChessBoard board) {
        if (inbounds(rowN, colN)) {
            System.out.printf("Inbounds %d,%d ", rowN, colN);


            if (board.getPiece(new ChessPosition(rowN,colN)) == null){
                movesList.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(rowN, colN), null));
                return true;}
            else if (board.getPiece(new ChessPosition(rowN,colN)).getTeamColor() == board.getPiece(new ChessPosition(row,col)).getTeamColor())
               return false;
            else
                movesList.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(rowN, colN), null));
                return false;

        } else {
            System.out.printf("Out of bounds %d,%d ", rowN, colN);
            return false;

        }
    }


    private Collection<ChessMove> bishopCalc(ChessPosition myPosition, ChessBoard board) {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        //left up
        boolean validMove = true;
        int rowN = row;
        int colN = col;
        while (validMove) {
            rowN = rowN - 1;
            colN = colN + 1;
            validMove = validMoveCheck(row, col, rowN, colN, board);
        }
            //upright
            validMove = true;
            rowN = row;
            colN = col;
            while (validMove) {
                rowN = rowN + 1;
                colN = colN + 1;
                validMove = validMoveCheck(row, col, rowN, colN, board);
            }
            //downright
            validMove = true;
            rowN = row;
            colN = col;
            while (validMove) {
                rowN = rowN + 1;
                colN = colN - 1;
                validMove = validMoveCheck(row, col, rowN, colN, board);
            }
            //downLeft
            validMove = true;
            rowN = row;
            colN = col;
            while (validMove) {
                rowN = rowN - 1;
                colN = colN - 1;
                validMove = validMoveCheck(row, col, rowN, colN, board);
            }


            return movesList;
        }

    private Collection<ChessMove> rookCalc(ChessPosition myPosition, ChessBoard board) {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // up
        boolean validMove = true;
        int rowN = row;
        int colN = col;
        while (validMove) {
            colN = colN + 1;
            validMove = validMoveCheck(row, col, rowN, colN, board);
        }
        //Right
        validMove = true;
        colN = col;
        while (validMove) {
            rowN = rowN + 1;
            validMove = validMoveCheck(row, col, rowN, colN, board);
        }
        //Down
        validMove = true;
        rowN = row;
        while (validMove) {
            colN = colN - 1;
            validMove = validMoveCheck(row, col, rowN, colN, board);
        }
        //Left
        validMove = true;
        colN = col;
        while (validMove) {
            rowN = rowN - 1;
            validMove = validMoveCheck(row, col, rowN, colN, board);
        }


        return movesList;
    }
    private Collection<ChessMove> queenCalc(ChessPosition myPosition, ChessBoard board) {




        bishopCalc(myPosition, board);

        rookCalc(myPosition, board);



        return movesList;
    }
    }
