package chess;

import java.util.Objects;

public class ChessMove {
    ChessPosition start, end;
    ChessPiece.PieceType type;

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
    @Override
    public String toString() {
        return "Start: " + getStartPosition().getRow() + ", " + getStartPosition().getColumn() + " End: " + getEndPosition().getRow() + ", " + getEndPosition().getColumn() + " " + type;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        start = startPosition;
        end = endPosition;
        type = promotionPiece;
    }

    public ChessPosition getStartPosition() {
        return start;
    }

    public ChessPosition getEndPosition() {
        return end;
    }

    public ChessPiece.PieceType getPromotionPiece() {
        if (getEndPosition().getRow() == 1 || getEndPosition().getRow() == 8) {
            return type;
        }
        return null;
    }
}
