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
        Collection<ChessMove> moves = cboard.getPiece(startPosition).pieceMoves(cboard, startPosition); //Issue about being static because it was not assigned to a piece, or the cboard.getPiece(startPosition) was not yet implemented
        ChessBoard tempboard = new ChessBoard();
        ChessPosition temp = new ChessPosition(1, 1);
        for (ChessMove current : moves){
//            for (int i = 1; i <=8; i++) { //Creates a temporary reconstruction of the board to mess with to determine legality of moves
//                temp.changeRow(i);
//                for (int j = 1; j <=8; j++) {
//                    temp.changeColumn(j);
//                    tempboard.addPiece(temp, cboard.getPiece(temp));
//                }
//            }
            tempboard = cboard;
            tempboard.addPiece(current.getEndPosition(), tempboard.getPiece(current.getStartPosition())); //Sets old piece to new location
            tempboard.addPiece(current.getStartPosition(), null); //Wipes old location as null
            if (isInCheck(currentColor, tempboard)){
                moves.remove(current);
            }
        }
        return moves;
        //Take input position on the chessboard, return all moves legal to make
        //Only valid if it's a piece move for the piece at the input location
        //Only valid if it does not place the King into check
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        //if (cboard.getPiece(move.getStartPosition()).getTeamColor() == currentColor){ //Checks who's team it is, need a way to update this unless the tests already do? Not sure, failing earlier, maybe invert?
            Collection<ChessMove> answers = validMoves(move.getStartPosition());
            if (answers.contains(move)) {
                cboard.addPiece(move.getEndPosition(), cboard.getPiece(move.getStartPosition())); //Sets old piece to new location
                cboard.addPiece(move.getStartPosition(), null); //Wipes old location as null
            }
            else{
                throw new InvalidMoveException();
            }
        //}
//        else{
//            throw new InvalidMoveException();
//        }
    }

    public void findKing(TeamColor color, ChessBoard board, ChessPosition returnpos) { //Finds king of set color
        for (int i = 1; i <=8; i++){
            returnpos.changeRow(i);
            for (int j = 1; j <=8; j++){
                returnpos.changeColumn(j);
                if(board.getPiece(returnpos) != null && board.getPiece(returnpos).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(returnpos).getTeamColor() == color) {
                    break;
                }
            }
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingpos = new ChessPosition(1, 1);
        findKing(teamColor, cboard, kingpos); //Finds king position
        Collection<ChessMove> kingmoves = cboard.getPiece(kingpos).pieceMoves(cboard, kingpos); //Finds valid moves for king, null if none
        ChessPosition current = new ChessPosition(1, 1); //Starts at 1, 1
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j); //Traverses board
                Collection<ChessMove> possibleattack = cboard.getPiece(current).pieceMoves(cboard, current); //Collects possible attacks for pieces
                for (int k=0; k<= possibleattack.size(); k++){ //Loops through possible attacks
                    ChessMove attack = new ChessMove(current, kingpos, cboard.getPiece(current).getPieceType()); //Temporary move where the attack lands on the King
                    if(possibleattack.contains(attack) && (kingmoves != null)) { //If this attack is true and the King can still move out of the way
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) { //Checks if in check in a separate board
        ChessPosition kingpos = new ChessPosition(1, 1);
        findKing(teamColor, cboard, kingpos); //Finds king position
        Collection<ChessMove> kingmoves = board.getPiece(kingpos).pieceMoves(board, kingpos); //Finds valid moves for king, null if none
        ChessPosition current = new ChessPosition(1, 1); //Starts at 1, 1
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j); //Traverses board
                if(board.getPiece(current) != null) {
                    Collection<ChessMove> possibleattack = board.getPiece(current).pieceMoves(board, current); //Collects possible attacks for pieces
                    for (int k = 0; k <= possibleattack.size(); k++) { //Loops through possible attacks
                        ChessMove attack = new ChessMove(current, kingpos, board.getPiece(current).getPieceType()); //Temporary move where the attack lands on the King
                        if (possibleattack.contains(attack) && (kingmoves != null)) { //If this attack is true and the King can still move out of the way
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingpos = new ChessPosition(1, 1);
        findKing(teamColor, cboard, kingpos); //Finds king position
        Collection<ChessMove> kingmoves = cboard.getPiece(kingpos).pieceMoves(cboard, kingpos);
        ChessPosition current = new ChessPosition(1, 1);
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j);
                Collection<ChessMove> possibleattack = cboard.getPiece(current).pieceMoves(cboard, current);
                for (int k=0; k<= possibleattack.size(); k++){
                    ChessMove attack = new ChessMove(current, kingpos, cboard.getPiece(current).getPieceType());
                    if(possibleattack.contains(attack) && (kingmoves == null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingpos = new ChessPosition(1, 1);
        findKing(teamColor, cboard, kingpos); //Finds king position
        Collection<ChessMove> kingmoves = board.getPiece(kingpos).pieceMoves(board, kingpos);
        ChessPosition current = new ChessPosition(1, 1);
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j);
                Collection<ChessMove> possibleattack = board.getPiece(current).pieceMoves(board, current);
                for (int k=0; k<= possibleattack.size(); k++){
                    ChessMove attack = new ChessMove(current, kingpos, board.getPiece(current).getPieceType());
                    if(possibleattack.contains(attack) && (kingmoves == null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingpos = new ChessPosition(1, 1);
        findKing(teamColor, cboard, kingpos); //Finds king position
        Collection<ChessMove> kingmoves = cboard.getPiece(kingpos).pieceMoves(cboard, kingpos);
        ChessPosition current = new ChessPosition(1, 1);
        for (int i = 1; i <=8; i++){
            current.changeRow(i);
            for (int j = 1; j <=8; j++){
                current.changeColumn(j);
                Collection<ChessMove> possibleattack = cboard.getPiece(current).pieceMoves(cboard, current);
                for (int k=0; k<= possibleattack.size(); k++){
                    ChessMove attack = new ChessMove(current, kingpos, cboard.getPiece(current).getPieceType());
                    if(!(possibleattack.contains(attack)) && (kingmoves == null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setBoard(ChessBoard board) {
        cboard = board;
    }

    public ChessBoard getBoard() {
        return cboard;
    }
}
