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
        int row = position.getRow();
        int col = position.getColumn();
        board[row][col] = piece;
    }

    public ChessPiece getPiece(ChessPosition position) { //Get the piece at the position or null of no piece at that position
        int row = position.getRow();
        int col = position.getColumn();
        return board[row][col];
    }

    public void resetBoard() { //Set board to normal setup
        ChessPiece temp = null;
        temp.Type = ChessPiece.PieceType.PAWN;
        temp.color = ChessGame.TeamColor.WHITE; //WHITE ON TOP
        for (int i = 0; i <= 8; i++) {
            board[6][i] = temp;
        }
        temp.Type = ChessPiece.PieceType.ROOK;
        board[7][0] = temp;
        board[7][7] = temp;
        temp.Type = ChessPiece.PieceType.KNIGHT;
        board[7][1] = temp;
        board[7][6] = temp;
        temp.Type = ChessPiece.PieceType.BISHOP;
        board[7][2] = temp;
        board[7][5] = temp;
        temp.Type = ChessPiece.PieceType.KING;
        board[7][3] = temp;
        temp.Type = ChessPiece.PieceType.QUEEN;
        board[7][4] = temp;

        temp.Type = ChessPiece.PieceType.PAWN;
        temp.color = ChessGame.TeamColor.BLACK; //BLACK ON BOTTOM
        for (int i = 0; i <= 8; i++) {
            board[1][i] = temp;
        }
        temp.Type = ChessPiece.PieceType.ROOK;
        board[0][0] = temp;
        board[0][7] = temp;
        temp.Type = ChessPiece.PieceType.KNIGHT;
        board[0][1] = temp;
        board[0][6] = temp;
        temp.Type = ChessPiece.PieceType.BISHOP;
        board[0][2] = temp;
        board[0][5] = temp;
        temp.Type = ChessPiece.PieceType.KING;
        board[0][3] = temp;
        temp.Type = ChessPiece.PieceType.QUEEN;
        board[0][4] = temp;
    }
}