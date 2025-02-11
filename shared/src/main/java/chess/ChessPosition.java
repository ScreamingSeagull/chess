package chess;

import java.util.Objects;

public class ChessPosition {
    int Row;
    int Col;

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
    public void changeRow(int row) {
        Row = row;
    }
    public void changeColumn(int col) {
        Col = col;
    }
}
