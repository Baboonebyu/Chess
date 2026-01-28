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
    private ChessPosition whiteKing;
    private ChessPosition blackKing;
    



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
        ChessPiece Piece = currentBoard.getPiece(startPosition);
        if( Piece == null){
            return List.of();
        }
        //todo
        // move the player turn and no piece functions
        // but the no piece function needs to stay for move list.

        Collection<ChessMove> moveList = Piece.pieceMoves(currentBoard, startPosition);
            //todo
            // add in things to remove the moves that are invalid
            // check from the king outward?
            // for castling have variables for each rook and each king.


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
                if(piece.getPieceType() == ChessPiece.PieceType.KING){
                    if (playerTurn == TeamColor.WHITE){
                        whiteKing=end;
                    }
                    else
                        blackKing = end;
                }
                currentBoard.addPiece(end,piece);
            } else {
                TeamColor color = piece.getTeamColor();
                currentBoard.addPiece(end, new ChessPiece(color,promote));
            }
            if(piece.getTeamColor() == TeamColor.WHITE){ playerTurn = TeamColor.BLACK;}
            else
                playerTurn = TeamColor.WHITE;
        }
        else
            throw new InvalidMoveException("Make move was given an Invalid move");

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

        System.out.printf("The king for team %s is at %s", teamColor, kingPosition);

        Collection<ChessPosition> otherTeamEndPositions = new ArrayList<ChessPosition>();
        for (int i = 1; i<9; i++){
            for (int j = 1; j<9; j++) {
                ChessPiece observedPiece = currentBoard.getPiece(new ChessPosition(i, j));
                if (observedPiece != null) {
                    if (observedPiece.getTeamColor() != teamColor) {
                        Collection<ChessMove> otherTeamMoves = observedPiece.pieceMoves(currentBoard, new ChessPosition(i, j));
                        for (ChessMove move : otherTeamMoves) {
                            otherTeamEndPositions.add(move.getEndPosition());
                        }


                    }

                }

            }
        }
        return otherTeamEndPositions.contains(kingPosition);

    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        if ( teamColor == TeamColor.WHITE){
            if (whiteKing == null){
                //find king
                for (int i = 1; i<9; i++){
                    for (int j = 1; j<9; j++){
                        if(currentBoard.getPiece(new ChessPosition(i,j))!= null) {
                        ChessPiece observed = currentBoard.getPiece(new ChessPosition(i,j));
                        if (observed != null) {
                            if (observed.getPieceType() == ChessPiece.PieceType.KING && observed.getTeamColor() == teamColor) {
                                kingPosition = new ChessPosition(i, j);
                                whiteKing = new ChessPosition(i, j);
                                break;
                            }
                        }
                    }
                    }
                }
            }
            else{
                kingPosition = whiteKing;
            }
        }
        else
        {
            if (blackKing == null){
                //find king
                for (int i = 1; i<9; i++){
                    for (int j = 1; j<9; j++){

                        if(currentBoard.getPiece(new ChessPosition(i,j))!= null) {
                            ChessPiece observed = currentBoard.getPiece(new ChessPosition(i, j));

                            if (observed.getPieceType() == ChessPiece.PieceType.KING && observed.getTeamColor() == teamColor) {
                                kingPosition = new ChessPosition(i, j);
                                blackKing = new ChessPosition(i, j);
                                break;
                            }
                        }
                    }
                }
            }
            else{
                kingPosition = blackKing;
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
            return false;
        }

        return checkForAnyMoves(teamColor);


    }

    private boolean checkForAnyMoves(TeamColor teamColor) {
        for (int i = 1; i<9; i++){
            for (int j = 1; j<9; j++) {
                if(currentBoard.getPiece(new ChessPosition(i,j)) != null){
                    if(currentBoard.getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor){
                        if(validMoves(new ChessPosition(i,j))!= null){
                            return false;
                        }
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
            return false;
        }
        if(isInCheckmate(teamColor)){return false;}

        return checkForAnyMoves(teamColor);
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
