package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor playerTurn = TeamColor.WHITE;
    private ChessBoard currentBoard = new ChessBoard();

    



    public ChessGame() {
    currentBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return playerTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        playerTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = currentBoard.getPiece(startPosition);

        if( piece == null){
            return List.of();
        }
        TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moveList = piece.pieceMoves(currentBoard, startPosition);
        Collection<ChessMove> needsRemoved = new ArrayList<>();
            //todo
            // for castling have variables for each rook and each king.

        // this should prevent moving into check.


            for (ChessMove move: moveList) {


                ChessPiece putback = currentBoard.getPiece(move.getEndPosition());
                currentBoard.addPiece(move.getEndPosition(), piece);
                currentBoard.addPiece(move.getStartPosition(), null);
                if (isInCheck(color)) {
                    needsRemoved.add(move);


                }
                currentBoard.addPiece(move.getEndPosition(), putback);
                currentBoard.addPiece(move.getStartPosition(),piece);

            }


        moveList.removeAll(needsRemoved);
      //  System.out.println(moveList);

        //todo
        // remove moves
        // make sure moves stop check
        return moveList;




    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promote = move.getPromotionPiece();
        Collection<ChessMove> moveList = validMoves(start);
        ChessPiece piece = currentBoard.getPiece(start);

        if( piece == null){
            throw new InvalidMoveException("There is no piece");
        }

        if(piece.getTeamColor() != playerTurn){
            throw new InvalidMoveException("Not your turn");
        }

        if(isInCheck(piece.getTeamColor())){
            throw new InvalidMoveException("in check");
        }
        if( moveList.contains(move)){

            currentBoard.addPiece(start,null);
            if (promote == null){
                currentBoard.addPiece(end,piece);
            } else {
                TeamColor color = piece.getTeamColor();
                currentBoard.addPiece(end, new ChessPiece(color,promote));
            }
            if(piece.getTeamColor() == TeamColor.WHITE){ playerTurn = TeamColor.BLACK;}
            else {
                playerTurn = TeamColor.WHITE;
            }
        }
        else {
            throw new InvalidMoveException("Make move was given an Invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingPosition;
        //todo
        // check from the king outwards
        // how do i find the king?
        // go through each position till the correct king is found?
        kingPosition = findKing(teamColor);
        if (kingPosition == null){
            throw new RuntimeException("is check can't find the king");
        }

        //System.out.printf("The king for team %s is at %s", teamColor, kingPosition);

        Collection<ChessPosition> otherTeamEndPositions = new ArrayList<>();

        for (int i = 1; i<9; i++){
            for (int j = 1; j<9; j++) {
                ChessPiece observedPiece = currentBoard.getPiece(new ChessPosition(i, j));
                if (observedPiece != null && observedPiece.getTeamColor() != teamColor) {

                        Collection<ChessMove> otherTeamMoves = observedPiece.pieceMoves(currentBoard, new ChessPosition(i, j));
                        for (ChessMove move : otherTeamMoves) {
                            otherTeamEndPositions.add(move.getEndPosition());

                        }




                }

            }
        }
        //System.out.println(otherTeamEndPositions.contains(kingPosition));
        return otherTeamEndPositions.contains(kingPosition);

    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition kingPosition = null;
                //find king
                for (int i = 1; i<9; i++){
                    for (int j = 1; j<9; j++){

                        if(currentBoard.getPiece(new ChessPosition(i,j))!= null) {
                            ChessPiece observed = currentBoard.getPiece(new ChessPosition(i, j));

                            if (observed.getPieceType() == ChessPiece.PieceType.KING && observed.getTeamColor() == teamColor) {
                                kingPosition = new ChessPosition(i, j);
                                break;
                            }
                        }
                    }
                }


        return kingPosition;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            System.out.println("not in check - checkmate function");
            return false;
        }

      //  System.out.println(checkForAnyMoves(teamColor));

        return checkForAnyMoves(teamColor);


    }

    private boolean checkForAnyMoves(TeamColor teamColor) {
        for (int i = 1; i<9; i++){
            for (int j = 1; j<9; j++) {
                ChessPiece piece = currentBoard.getPiece(new ChessPosition(i, j));
                if(piece != null && piece.getTeamColor() == teamColor){
                    if(validMoves(new ChessPosition(i,j))!= null && !validMoves(new ChessPosition(i,j)).isEmpty() ){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            System.out.println("not stalemate but check");
            return false;
        }
        if(isInCheckmate(teamColor)){
            System.out.println("This is checkmate not stalemate");
            return false;}


        if (checkForAnyMoves(teamColor)){
            System.out.println("no vaid move");
            if(teamColor == playerTurn){
                System.out.println("is your turn");
                return true;
            }
        }

        return false;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return playerTurn == chessGame.playerTurn && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerTurn, currentBoard);
    }
}
