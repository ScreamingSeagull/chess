package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
//    @Override
//    public String toString() {
//        String expression = "";
//        for (int i = 0; i <=7; i++) {
//            for (int j = 0; j <=7; j++) {
//                if (board[i][j] == null) {
//                    expression = expression + "_ ";
//                }
//                else {
//                    expression = expression + board[i][j].getPieceType().toString() + " ";
//                }
//            }
//            expression = expression + "\n";
//        }
//        return expression;
//    }

    public ChessBoard() {
        
    }
    ChessPiece[][] board = new ChessPiece[8][8];

    public void addPiece(ChessPosition position, ChessPiece piece) { //Add piece to the board, where to add and the piece to add
        int row = position.getRow();
        int col = position.getColumn();
        board[row-1][col-1] = piece;
    }

    public ChessPiece getPiece(ChessPosition position) { //Get the piece at the position or null of no piece at that position
        int row = position.getRow();
        int col = position.getColumn();
        return board[row-1][col-1];
    }

    public void resetBoard() { //Set board to normal setup
        ChessPiece bpawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN); //BLACK ON TOP
        for (int i = 0; i <= 7; i++) {
            board[6][i] = bpawn;
        }
        ChessPiece brook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[7][0] = brook;
        board[7][7] = brook;
        ChessPiece bknight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[7][1] = bknight;
        board[7][6] = bknight;
        ChessPiece bbishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[7][2] = bbishop;
        board[7][5] = bbishop;
        ChessPiece bqueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[7][3] = bqueen;
        ChessPiece bking = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[7][4] = bking;


        ChessPiece wpawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN); //WHITE on bottom
        for (int i = 0; i <= 7; i++) {
            board[1][i] = wpawn;
        }
        ChessPiece wrook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[0][0] = wrook;
        board[0][7] = wrook;
        ChessPiece wknight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[0][1] = wknight;
        board[0][6] = wknight;
        ChessPiece wbishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[0][2] = wbishop;
        board[0][5] = wbishop;
        ChessPiece wqueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[0][3] = wqueen;
        ChessPiece wking = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[0][4] = wking;


        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j <=7; j++) {
                board[i][j] = null;
            }
        }
    }
}