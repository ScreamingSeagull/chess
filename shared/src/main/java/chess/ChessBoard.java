package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        
    }
    ChessPosition current;
    ChessPiece selected;
    ChessPiece[][] board = new ChessPiece[8][8];

    public void addPiece(ChessPosition position, ChessPiece piece) { //Add piece to the board, where to add and the piece to add
        int row = ChessPosition.getRow();
        int col = ChessPosition.getColumn();
        board[row][col] = piece;
    }

    public ChessPiece getPiece(ChessPosition position) { //Get the piece at the position or null of no piece at that position
        int row = ChessPosition.getRow();
        int col = ChessPosition.getColumn();
        return board[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
//        ChessPiece pawnw = null;
//        pawnw.Type = ChessPiece.PieceType.PAWN;
//        pawnw.color = ChessGame.TeamColor.WHITE;
//        for (int i = 0; i <= 8; i++) {
//            board[1][i] = pawnw;
//        }
//        ChessPiece pawnb = null;
//        pawnb.Type = ChessPiece.PieceType.PAWN;
//        pawnb.color = ChessGame.TeamColor.BLACK;
//        for (int i = 0; i <= 8; i++) {
//            board[6][i] = pawnw;
//        }
//    }
    }
}