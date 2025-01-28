package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 */
public class ChessPosition {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return Row == that.Row && Col == that.Col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Row, Col);
    }

    int Row;
    int Col;
    
    public ChessPosition(int row, int col) {
        Row = row;
        Col = col;
    }

    public int getRow() {
        return Row;
    }

    public int getColumn() {
        return Col;
    }
}
