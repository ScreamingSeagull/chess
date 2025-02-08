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

    public ChessPosition findKing(TeamColor color) { //Finds king of set color
        ChessPosition temp = new ChessPosition(0, 0);
        for (int i = 0; i <=7; i++){
            temp.changeRow(i);
            for (int j = 0; j <=7; j++){
                temp.changeColumn(j);
                if(cboard.getPiece(temp).getPieceType() == ChessPiece.PieceType.KING && cboard.getPiece(temp).getTeamColor() == color) {
                    return temp;
                }
            }
        }
        return null; //There is no king remaining of set type
    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingpos = findKing(teamColor);
        Collection<ChessMove> kingmoves = cboard.getPiece(kingpos).pieceMoves(cboard, kingpos);
        ChessPosition current = new ChessPosition(0, 0);
        for (int i = 0; i <=7; i++){
            current.changeRow(i);
            for (int j = 0; j <=7; j++){
                current.changeColumn(j);
                Collection<ChessMove> possibleattack = cboard.getPiece(current).pieceMoves(cboard, current);
                for (int k=0; k<= possibleattack.size(); k++){
                    ChessMove attack = new ChessMove(current, kingpos, cboard.getPiece(current).getPieceType());
                    if(possibleattack.contains(attack) && (kingmoves != null)) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingpos = findKing(teamColor);
        Collection<ChessMove> kingmoves = cboard.getPiece(kingpos).pieceMoves(cboard, kingpos);
        ChessPosition current = new ChessPosition(0, 0);
        for (int i = 0; i <=7; i++){
            current.changeRow(i);
            for (int j = 0; j <=7; j++){
                current.changeColumn(j);
                Collection<ChessMove> possibleattack = cboard.getPiece(current).pieceMoves(cboard, current);
                for (int k=0; k<= possibleattack.size(); k++){
                    ChessMove attack = new ChessMove(current, kingpos, cboard.getPiece(current).getPieceType());
                    if(possibleattack.contains(attack) && (kingmoves == null)) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
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
