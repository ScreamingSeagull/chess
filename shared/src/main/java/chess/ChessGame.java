package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGame {
    TeamColor currentColor;
    ChessBoard cboard = new ChessBoard();

    public ChessGame() {
        currentColor = TeamColor.WHITE;
        cboard.resetBoard();
    }

    public TeamColor getTeamTurn() {
        return currentColor;
    }

    public void setTeamTurn(TeamColor team) {
        currentColor = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (cboard.getPiece(startPosition) == null) {
            return null;
        }
        Collection<ChessMove> realanswers = cboard.getPiece(startPosition).pieceMoves(cboard, startPosition); //Issue about being static because it was not assigned to a piece, or the cboard.getPiece(startPosition) was not yet implemented


        return null;
        //Take input position on the chessboard, return all moves legal to make
        //Only valid if it's a piece move for the piece at the input location
        //Only valid if it does not place the King into check
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (cboard.getPiece(move.getStartPosition()).getTeamColor() == currentColor){
            Collection<ChessMove> answers = validMoves(move.getStartPosition());
            if (answers.contains(move)) {
                cboard.addPiece(move.getEndPosition(), cboard.getPiece(move.getStartPosition())); //Sets old piece to new location
                cboard.addPiece(move.getStartPosition(), null); //Wipes old location as null
            }
            else{
                throw new InvalidMoveException();
            }
        }
        else{
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        cboard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return cboard;
    }
}
