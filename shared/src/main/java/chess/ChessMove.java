package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 */
public class ChessMove {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end) && type == chessMove.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, type);
    }

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
        return type;
    }
}
