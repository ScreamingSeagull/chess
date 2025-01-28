package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 */
public class ChessPiece {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && Type == that.Type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, Type);
    }

    ChessGame.TeamColor color;
    PieceType Type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        color = pieceColor;
        Type = type;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public ChessGame.TeamColor getTeamColor() { //Return the team color
        return color;
    }

    public PieceType getPieceType() { //Return which chess piece type this is
        return Type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.PAWN) {
            if (piece.color == ChessGame.TeamColor.WHITE) { //MOVE DOWNWARD, BLACK
                if (myPosition.getRow() >= 1)
                {
                    //
                }
            }
            else { //MOVE UPWARD, WHITE

            }
        }
        if (piece.getPieceType() == PieceType.ROOK) {
            //ROOK
        }
        if (piece.getPieceType() == PieceType.BISHOP) {
            //BISHOP
        }
        if (piece.getPieceType() == PieceType.KNIGHT) {
            //KNIGHT
        }
        if (piece.getPieceType() == PieceType.KING) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ArrayList finalanswers = new ArrayList();
            if () {
                ChessPosition move = new ChessPosition(1, 4);
                finalanswers.add(move);
            }

            return finalanswers;
        }
        if (piece.getPieceType() == PieceType.QUEEN) {
            //QUEEN
        }
        return List.of();
    }
}
