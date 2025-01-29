package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    ChessPosition start;
    ChessPosition end;
    ChessPiece.PieceType type;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        start = startPosition;
        end = endPosition;
        type = promotionPiece;
    }

    public ChessPosition getStartPosition() { //Return start position
        return start;
    }

    public ChessPosition getEndPosition() { //Return end location
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        if (type == ChessPiece.PieceType.PAWN) { //ADD CHECK HERE TO SEE IF PROMOTION IS ACCOUNTED FOR
            return ChessPiece.PieceType.QUEEN;
        }
        return null;
    }
}
