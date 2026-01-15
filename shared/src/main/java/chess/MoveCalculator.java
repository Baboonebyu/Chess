package chess;




import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveCalculator {
    private final List<ChessMove> movesList = new ArrayList<>();

    //Todo figure out promotion and side capturing for pawn

    public ChessBoard board;
    public ChessPosition myPosition;

    public MoveCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public Collection<ChessMove> pieceCalc() {


        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return bishopCalc();

        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return rookCalc();
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return queenCalc();
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return kingCalc();
        } else if ((piece.getPieceType() == ChessPiece.PieceType.KNIGHT)) {
            return knightCalc();
        } else if ((piece.getPieceType() == ChessPiece.PieceType.PAWN)) {
            return pawnCalc(piece.getTeamColor());

        }
        throw new RuntimeException("Not a valid type");
    }

    private  boolean inbounds(int newRow, int newCol) {
        if (newRow < 1 || newRow > 8)
            return false;
        else return newCol >= 1 && newCol <= 8;

    }

    private  boolean validMoveCheck(int row, int col, int rowN, int colN) {
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


    private Collection<ChessMove> bishopCalc() {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        //left up
        boolean validMove = true;
        int rowN = row;
        int colN = col;
        while (validMove) {
            rowN = rowN - 1;
            colN = colN + 1;
            validMove = validMoveCheck(row, col, rowN, colN);
        }
            //upright
            validMove = true;
            rowN = row;
            colN = col;
            while (validMove) {
                rowN = rowN + 1;
                colN = colN + 1;
                validMove = validMoveCheck(row, col, rowN, colN);
            }
            //downright
            validMove = true;
            rowN = row;
            colN = col;
            while (validMove) {
                rowN = rowN + 1;
                colN = colN - 1;
                validMove = validMoveCheck(row, col, rowN, colN);
            }
            //downLeft
            validMove = true;
            rowN = row;
            colN = col;
            while (validMove) {
                rowN = rowN - 1;
                colN = colN - 1;
                validMove = validMoveCheck(row, col, rowN, colN);
            }


            return movesList;
        }

    private Collection<ChessMove> rookCalc() {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // up
        boolean validMove = true;
        int rowN = row;
        int colN = col;
        while (validMove) {
            colN = colN + 1;
            validMove = validMoveCheck(row, col, rowN, colN);
        }
        //Right
        validMove = true;
        colN = col;
        while (validMove) {
            rowN = rowN + 1;
            validMove = validMoveCheck(row, col, rowN, colN);
        }
        //Down
        validMove = true;
        rowN = row;
        while (validMove) {
            colN = colN - 1;
            validMove = validMoveCheck(row, col, rowN, colN);
        }
        //Left
        validMove = true;
        colN = col;
        while (validMove) {
            rowN = rowN - 1;
            validMove = validMoveCheck(row, col, rowN, colN);
        }


        return movesList;
    }
    private Collection<ChessMove> queenCalc() {

        bishopCalc();
        rookCalc();

        return movesList;
    }

    private Collection<ChessMove> kingCalc() {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();



        int rowN = row;
        int colN = col;
        //left up
        rowN = row - 1;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN);

        //upright
        rowN = row + 1;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN);
        //downright
        rowN = row + 1;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN);
        //downLeft
        rowN = row - 1;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN);

        //left
        rowN = row - 1;
        colN = col;
        validMoveCheck(row, col, rowN, colN);
        //up
        rowN = row;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN);
        //down
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN);
        //Right
        rowN = row + 1;
        colN = col;
        validMoveCheck(row, col, rowN, colN);
        return movesList;
    }
    private Collection<ChessMove> knightCalc() {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();



        int rowN = row;
        int colN = col;
        //upLeft
        rowN = row + 2;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN);

        //upRight
        rowN = row + 2;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN);

        //rightUp
        rowN = row + 1;
        colN = col + 2;
        validMoveCheck(row, col, rowN, colN);
        //rightDown
        rowN = row - 1;
        colN = col + 2;
        validMoveCheck(row, col, rowN, colN);

        //downRight
        rowN = row - 2;
        colN = col + 1;
        validMoveCheck(row, col, rowN, colN);
        //downLeft
        rowN = row - 2;
        colN = col - 1;
        validMoveCheck(row, col, rowN, colN);
        //leftDown
        rowN = row - 1;
        colN = col - 2;

        validMoveCheck(row, col, rowN, colN);
        //leftUp
        rowN = row + 1;
        colN = col - 2;
        validMoveCheck(row, col, rowN, colN);
        return movesList;
    }

    private void promotionCheck(int row, int col, int rowN, int colN){
        if (rowN == 1 || rowN == 8) {
            movesList.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(rowN, colN), ChessPiece.PieceType.BISHOP));
            movesList.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(rowN, colN), ChessPiece.PieceType.ROOK));
            movesList.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(rowN, colN), ChessPiece.PieceType.KNIGHT));
            movesList.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(rowN, colN), ChessPiece.PieceType.QUEEN));
        }

        else
            movesList.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(rowN, colN), null));

    }
    private Collection<ChessMove> pawnCalc( ChessGame.TeamColor teamColor) {


        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int rowN = row;
        int colN = col;
        if (teamColor == ChessGame.TeamColor.WHITE){
            //System.out.println("This pawn is on team white");
            //forwardWhite
            rowN = row + 1;
            if (inbounds(rowN, colN) && board.getPiece(new ChessPosition(rowN,colN)) == null)
                promotionCheck(row, col, rowN, colN);
            if (row == 2) {
                rowN = row + 2;
                if ( board.getPiece(new ChessPosition(row+1,colN)) == null && board.getPiece(new ChessPosition(rowN,colN)) == null)
                    promotionCheck(row, col, rowN, colN);

            }
            //captureLeft
            colN = col -1;
            rowN = row + 1;
            if (inbounds(rowN,colN) && board.getPiece(new ChessPosition(rowN,colN))!= null)
                if(board.getPiece(new ChessPosition(row,col)).getTeamColor() != board.getPiece(new ChessPosition(rowN,colN)).getTeamColor())
                    promotionCheck(row, col, rowN, colN);
            //captureRight
            colN = col +1;
            if (inbounds(rowN,colN) && board.getPiece(new ChessPosition(rowN,colN))!= null)
                if(board.getPiece(new ChessPosition(row,col)).getTeamColor() != board.getPiece(new ChessPosition(rowN,colN)).getTeamColor())
                    promotionCheck(row, col, rowN, colN);
        }
        if (teamColor == ChessGame.TeamColor.BLACK) {
            //System.out.println("This pawn is on team Black");
            //forwardBlack
            rowN = row - 1;
            if (inbounds(rowN, colN) && board.getPiece(new ChessPosition(rowN,colN)) == null)
                promotionCheck(row, col, rowN, colN);
            if (row == 7 ){
                rowN = row - 2;
                if ( board.getPiece(new ChessPosition(row-1,colN)) == null && board.getPiece(new ChessPosition(rowN,colN)) == null)
                    promotionCheck(row, col, rowN, colN);
            }
            //captureLeft
            colN = col -1;
            rowN = row - 1;
            if (inbounds(rowN,colN) && board.getPiece(new ChessPosition(rowN,colN))!= null)
                if(board.getPiece(new ChessPosition(row,col)).getTeamColor() != board.getPiece(new ChessPosition(rowN,colN)).getTeamColor())
                    promotionCheck(row, col, rowN, colN);
            //captureRight
            colN = col +1;
            if (inbounds(rowN,colN) && board.getPiece(new ChessPosition(rowN,colN))!= null)
                if(board.getPiece(new ChessPosition(row,col)).getTeamColor() != board.getPiece(new ChessPosition(rowN,colN)).getTeamColor())
                    promotionCheck(row, col, rowN, colN);
        }
        return movesList;

    }
    }
