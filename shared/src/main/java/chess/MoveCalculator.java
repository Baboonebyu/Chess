package chess;




import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveCalculator {
    private final List<ChessMove> movesList = new ArrayList<>();
    //Todo create a constructor? so the varables i need can be passed in
    //Todo quit recalculating the piece and passing the board
    //Todo figure out promotion and side capturing for pawn




    public Collection<ChessMove> pieceCalc(ChessBoard board, ChessPosition myPosition) {


        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return bishopCalc(myPosition,board);

        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return rookCalc(myPosition,board);
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return queenCalc(myPosition,board);
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return kingCalc(myPosition,board);
        } else if ((piece.getPieceType() == ChessPiece.PieceType.KNIGHT)) {
            return knightCalc(myPosition,board);
        } else if ((piece.getPieceType() == ChessPiece.PieceType.PAWN)) {
            return pawnCalc(myPosition,board,piece.getTeamColor());

        }
        throw new RuntimeException("Not a valid type");
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

    private Collection<ChessMove> kingCalc(ChessPosition myPosition, ChessBoard board) {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();



        int rowN = row;
        int colN = col;
        //left up
        rowN = row - 1;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN, board);

        //upright
        rowN = row + 1;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN, board);
        //downright
        rowN = row + 1;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);
        //downLeft
        rowN = row - 1;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);

        //left
        rowN = row - 1;
        colN = col;
        validMoveCheck(row, col, rowN, colN, board);
        //up
        rowN = row;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN, board);
        //down
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);
        //Right
        rowN = row + 1;
        colN = col;
        validMoveCheck(row, col, rowN, colN, board);
        return movesList;
    }
    private Collection<ChessMove> knightCalc(ChessPosition myPosition, ChessBoard board) {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();



        int rowN = row;
        int colN = col;
        //upLeft
        rowN = row + 2;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);

        //upRight
        rowN = row + 2;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN, board);

        //rightUp
        rowN = row + 1;
        colN = col + 2;
        validMoveCheck(row, col, rowN, colN, board);
        //rightDown
        rowN = row - 1;
        colN = col + 2;
        validMoveCheck(row, col, rowN, colN, board);

        //downRight
        rowN = row - 2;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN, board);
        //downLeft
        rowN = row - 2;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);
        //leftDown
        rowN = row - 1;
        colN = col - 2;

        validMoveCheck(row, col, rowN, colN, board);
        //leftUp
        rowN = row + 1;
        colN = col - 2;
        validMoveCheck(row, col, rowN, colN, board);
        return movesList;
    }

    private Collection<ChessMove> pawnCalc(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor teamColor) {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int rowN = row;
        int colN = col;
        if (teamColor == ChessGame.TeamColor.WHITE){
            System.out.println("This pawn is on team white");
            //forwardWhite
            rowN = row + 1;
            validMoveCheck(row, col, rowN, colN, board);
            if (row == 2) {
                rowN = row + 2;
                validMoveCheck(row, col, rowN, colN, board);
            }
        }
        if (teamColor == ChessGame.TeamColor.BLACK) {
            System.out.println("This pawn is on team Black");
            //forwardBlack
            rowN = row - 1;
            validMoveCheck(row, col, rowN, colN, board);
            if (row == 7) {
                rowN = row - 2;
                validMoveCheck(row, col, rowN, colN, board);
            }
        }
        return movesList;


        /**
        //left up
        rowN = row - 1;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN, board);

        //upright
        rowN = row + 1;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN, board);
        //downright
        rowN = row + 1;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);
        //downLeft
        rowN = row - 1;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);

        //left
        rowN = row - 1;
        colN = col;
        validMoveCheck(row, col, rowN, colN, board);

        //down
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN, board);
        //Right
        rowN = row + 1;
        colN = col;
        validMoveCheck(row, col, rowN, colN, board);
        return movesList;
         */
    }
    }
