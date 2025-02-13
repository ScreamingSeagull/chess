package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
        Collection<ChessMove> moves = cboard.getPiece(startPosition).pieceMoves(cboard, startPosition); //Issue about being static because it was not assigned to a piece, or the cboard.getPiece(startPosition) was not included
        Collection<ChessMove> answers = cboard.getPiece(startPosition).pieceMoves(cboard, startPosition); //Setting answers equal to moves results in them just pointing to each other? Breaks modification of collection while iterating through it below
        ChessBoard tempboard = new ChessBoard();
        ChessPosition temp = new ChessPosition(1, 1);
        for (ChessMove current : moves){
            for (int i = 0; i <8; i++) { //Creates a temporary reconstruction of the board to mess with to determine legality of moves
                temp.changeRow(i+1);
                for (int j = 0; j <8; j++) {
                    temp.changeColumn(j+1);
                    tempboard.addPiece(temp, cboard.getPiece(temp));
                }
            }
            tempboard.addPiece(current.getEndPosition(), tempboard.getPiece(current.getStartPosition())); //Sets old piece to new location
            tempboard.addPiece(current.getStartPosition(), null); //Wipes old location as null
            if (isInCheck(currentColor, tempboard) || isInCheckmate(currentColor, tempboard)){
                answers.remove(current);
            }
        }
        return answers;
        //Take input position on the chessboard, return all moves legal to make
        //Only valid if it's a piece move for the piece at the input location
        //Only valid if it does not place the King into check
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (cboard.getPiece(move.getStartPosition()) == null){
            //No item found
        }
        else if (cboard.getPiece(move.getStartPosition()).getTeamColor() == currentColor){
            Collection<ChessMove> answers = validMoves(move.getStartPosition());
            if (answers.contains(move)) {
                cboard.addPiece(move.getEndPosition(), cboard.getPiece(move.getStartPosition())); //Sets old piece to new location
                cboard.addPiece(move.getStartPosition(), null); //Wipes old location as null
                if (currentColor == TeamColor.WHITE) currentColor = TeamColor.BLACK;
                else currentColor = TeamColor.WHITE;
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    public ChessPosition findKing(TeamColor color, ChessBoard board) { //Finds king of set color
        ChessPosition returnpos = new ChessPosition(1, 1);
        for (int i = 1; i <=8; i++){
            returnpos.changeColumn(i);
            for (int j = 1; j <=8; j++){
                returnpos.changeRow(j);
                if(board.getPiece(returnpos) != null && (board.getPiece(returnpos).getPieceType() == ChessPiece.PieceType.KING) && board.getPiece(returnpos).getTeamColor() == color) {
                    return returnpos;
                }
            }
        }
        return null;
    }

    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, cboard);
    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) { //Checks if in check in a separate board
        ChessPosition kingpos = findKing(teamColor, board); //Finds king position
        Collection<ChessMove> kingmoves = board.getPiece(kingpos).pieceMoves(board, kingpos); //Finds valid moves for king, null if none
        ChessPosition current = new ChessPosition(1, 1); //Starts at 1, 1
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j); //Traverses board
                if(board.getPiece(current) != null) {
                    Collection<ChessMove> possibleattack = board.getPiece(current).pieceMoves(board, current); //Collects possible attacks for pieces
                    ChessMove attack = new ChessMove(current, kingpos, null); //Temporary move where the attack lands on the King
                    if (possibleattack.contains(attack) && !kingmoves.isEmpty()) { //If this attack is true and the King can still move out of the way
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheckmate(teamColor, cboard);
    }

    public boolean isInCheckmate(TeamColor teamColor, ChessBoard board) { //ERROR WITH SWITCHING BOARD WAS MANIPULATING INPUT BOARD BUT USING CLASS BOARD
        ChessPosition kingpos = findKing(teamColor, board); //Finds king position
        Collection<ChessMove> kingmoves = board.getPiece(kingpos).pieceMoves(board, kingpos);
        Collection<ChessMove> actualmoves = board.getPiece(kingpos).pieceMoves(board, kingpos);
        for (ChessMove possible : kingmoves) {
            if (!kingMove(currentColor, possible, board)) {
                actualmoves.remove(possible);
            }
        }
        ChessPosition current = new ChessPosition(1, 1);
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j);
                if(board.getPiece(current) != null) {
                    Collection<ChessMove> possibleattack = board.getPiece(current).pieceMoves(board, current);
                    ChessMove attack = new ChessMove(current, kingpos, null);
                    if (possibleattack.contains(attack) && actualmoves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean kingMove(TeamColor color, ChessMove move, ChessBoard board) { //RETURNS FALSE IF NOT ABLE TO MOVE INTO SET SPACE
        ChessBoard tempboard = new ChessBoard();
        ChessPosition temp = new ChessPosition(1, 1);
        for (int i = 0; i < 8; i++) { //Creates a temporary reconstruction of the board to mess with to determine legality of moves
            temp.changeRow(i + 1);
            for (int j = 0; j < 8; j++) {
                temp.changeColumn(j + 1);
                tempboard.addPiece(temp, board.getPiece(temp));
            }
        }
        tempboard.addPiece(move.getEndPosition(), tempboard.getPiece(move.getStartPosition()));
        tempboard.addPiece(move.getStartPosition(), null);
        ChessPosition current = new ChessPosition(1, 1);
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j);
                if(tempboard.getPiece(current) != null && tempboard.getPiece(current).getTeamColor() != color) {
                    Collection<ChessMove> possibleattack = tempboard.getPiece(current).pieceMoves(tempboard, current);
                    ChessMove attack = new ChessMove(current, move.getEndPosition(), null);
                    if (possibleattack.contains(attack)) { //If the King enters a check/checkmate position
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        return false;
//        ChessPosition kingpos = findKing(teamColor, cboard); //Finds king position
//        Collection<ChessMove> kingmoves = cboard.getPiece(kingpos).pieceMoves(cboard, kingpos);
//        ChessPosition current = new ChessPosition(1, 1);
//        for (int i = 1; i <=8; i++){
//            current.changeRow(i);
//            for (int j = 1; j <=8; j++){
//                current.changeColumn(j);
//                if(cboard.getPiece(current) != null) {
//                    Collection<ChessMove> possibleattack = cboard.getPiece(current).pieceMoves(cboard, current);
//                    for (int k = 0; k <= possibleattack.size(); k++) {
//                        ChessMove attack = new ChessMove(current, kingpos, cboard.getPiece(current).getPieceType());
//                        if (!(possibleattack.contains(attack))) { //************************************************************Set team has no valid moves, not just king
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
    }

    public void setBoard(ChessBoard board) {
        cboard = board;
    }

    public ChessBoard getBoard() {
        return cboard;
    }
}
