package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ChessPiece {
    ChessGame.TeamColor color;
    PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    public PieceType getPieceType() {
        return type;
    }

    public void setPieceType(ChessPiece.PieceType promotion) {
        type = promotion;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> answers = new ArrayList();
        if (board.getPiece(myPosition) == null) {
            return null;
        }
        if (type == ChessPiece.PieceType.PAWN) {
            pawn(board, myPosition, color, row, col, answers);
        }
        if (type == ChessPiece.PieceType.ROOK) {
            rook(board, myPosition, row, col, answers, color);
        }
        if (type == ChessPiece.PieceType.KNIGHT) {
            knight(board, myPosition, row, col, color, answers);
        }
        if (type == ChessPiece.PieceType.BISHOP) {
            bishop(board, myPosition, row, col, answers, color);
        }
        if (type == ChessPiece.PieceType.QUEEN) {
            rook(board, myPosition, row, col, answers, color);
            bishop(board, myPosition, row, col, answers, color);
        }
        if (type == ChessPiece.PieceType.KING){
            king(board, myPosition, row, col, color, answers);
        }
        return answers;
    }

    private static void king(ChessBoard board, ChessPosition myPosition, int row, int col, ChessGame.TeamColor color, Collection<ChessMove> answers) {
        if (row < 8) { //Upwards
            if (board.getPiece(row +1, col) == null || (board.getPiece(row +1, col).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row +1, col);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
            if (col - 1 > 1 && (board.getPiece(row +1, col -1) == null || (board.getPiece(row +1, col -1).getTeamColor() != color))) {
                ChessPosition spot = new ChessPosition(row +1, col -1);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
            if (col + 1 < 8 && (board.getPiece(row +1, col +1) == null || (board.getPiece(row +1, col +1).getTeamColor() != color))) {
                ChessPosition spot = new ChessPosition(row +1, col +1);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
        }
        if (row > 1) { //Downwards
            if (board.getPiece(row -1, col) == null || (board.getPiece(row -1, col).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row -1, col);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
            if (col - 1 > 1 && (board.getPiece(row -1, col -1) == null || (board.getPiece(row -1, col -1).getTeamColor() != color))) {
                ChessPosition spot = new ChessPosition(row -1, col -1);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
            if (col + 1 < 8 && (board.getPiece(row -1, col +1) == null || (board.getPiece(row -1, col +1).getTeamColor() != color))) {
                ChessPosition spot = new ChessPosition(row -1, col +1);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
        }
        if (col > 1) {
            if (board.getPiece(row, col -1) == null || (board.getPiece(row, col -1).getTeamColor() != color)){
                ChessPosition spot = new ChessPosition(row, col -1);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
        }
        if (col < 8) {
            if (board.getPiece(row, col +1) == null || (board.getPiece(row, col +1).getTeamColor() != color)){
                ChessPosition spot = new ChessPosition(row, col +1);
                ChessMove answer = new ChessMove(myPosition, spot, null);
                answers.add(answer);
            }
        }
    }

    private static void bishop(ChessBoard board, ChessPosition position, int row, int col, Collection<ChessMove> answers, ChessGame.TeamColor color) {
        int temprow = row;
        int tempcol = col;
        while (temprow < 8 && tempcol < 8) { //UP RIGHT
            if(board.getPiece(temprow+1, tempcol+1) == null) {
                ChessPosition spot = new ChessPosition(temprow+1, tempcol+1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                temprow++;
                tempcol++;
            }
            else if(board.getPiece(temprow+1,tempcol+1).getTeamColor() != color) {
                ChessPosition spot = new ChessPosition(temprow+1, tempcol+1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                break;
            }
            else {break;}
        }
        temprow = row;
        tempcol = col;
        while (temprow > 1 && tempcol < 8) { //DOWN RIGHT
            if(board.getPiece(temprow-1, tempcol+1) == null) {
                ChessPosition spot = new ChessPosition(temprow-1, tempcol+1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                temprow--;
                tempcol++;
            }
            else if(board.getPiece(temprow-1,tempcol+1).getTeamColor() != color) {
                ChessPosition spot = new ChessPosition(temprow-1, tempcol+1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                break;
            }
            else {break;}
        }
        temprow = row;
        tempcol = col;
        while (temprow > 1 && tempcol > 1) { //DOWN LEFT
            if(board.getPiece(temprow-1, tempcol-1) == null) {
                ChessPosition spot = new ChessPosition(temprow-1, tempcol-1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                temprow--;
                tempcol--;
            }
            else if(board.getPiece(temprow-1,tempcol-1).getTeamColor() != color) {
                ChessPosition spot = new ChessPosition(temprow-1, tempcol-1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                break;
            }
            else {break;}
        }
        temprow = row;
        tempcol = col;
        while (temprow < 8 && tempcol > 1) { //UP LEFT
            if(board.getPiece(temprow+1, tempcol-1) == null) {
                ChessPosition spot = new ChessPosition(temprow+1, tempcol-1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                temprow++;
                tempcol--;
            }
            else if(board.getPiece(temprow+1,tempcol-1).getTeamColor() != color) {
                ChessPosition spot = new ChessPosition(temprow+1, tempcol-1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
                break;
            }
            else {break;}
        }
    }

    private static void knight(ChessBoard board, ChessPosition position, int row, int col, ChessGame.TeamColor color, Collection<ChessMove> answers) {
        if (row - 2 >= 1) { //Downwards
            if (col - 1 >= 1 && ((board.getPiece(row - 2, col - 1) == null || board.getPiece(row - 2, col - 1).getTeamColor() != color))) {
                ChessPosition spot = new ChessPosition(row - 2, col - 1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
            if (col + 1 <= 8 && (board.getPiece(row - 2, col + 1) == null || board.getPiece(row - 2, col + 1).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row - 2, col + 1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
        }
        if (row + 2 <= 8) { //Upwards
            if (col - 1 >= 1 && (board.getPiece(row + 2, col - 1) == null || board.getPiece(row + 2, col - 1).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row + 2, col - 1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
            if (col + 1 <= 8 && (board.getPiece(row + 2, col + 1) == null || board.getPiece(row + 2, col + 1).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row + 2, col + 1);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
        }
        if (col + 2 <= 8) { //Right
            if (row - 1 >= 1 && (board.getPiece(row - 1, col + 2) == null || board.getPiece(row - 1, col + 2).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row - 1, col + 2);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
            if (row + 1 <= 8 && (board.getPiece(row + 1, col + 2) == null || board.getPiece(row + 1, col + 2).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row + 1, col + 2);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
        }
        if (col - 2 >= 1) { //Left
            if (row - 1 >= 1 && (board.getPiece(row - 1, col - 2) == null || board.getPiece(row - 1, col - 2).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row - 1, col - 2);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
            if (row + 1 <= 8 && (board.getPiece(row + 1, col - 2) == null || board.getPiece(row + 1, col - 2).getTeamColor() != color)) {
                ChessPosition spot = new ChessPosition(row + 1, col - 2);
                ChessMove answer = new ChessMove(position, spot, null);
                answers.add(answer);
            }
        }
    }

    private static void rook(ChessBoard board, ChessPosition myPosition, int row, int col, Collection<ChessMove> answers, ChessGame.TeamColor color) {
        if (row < 8) { //Moves up
            for (int i = row + 1; i <= 8; i++) {
                if (rookMove(board, myPosition, col, answers, color, i)) {
                    break;
                }
            }
        }
        if (row > 1) { //Moves down
            for (int i = row - 1; i >= 1; i--) { //Moves down
                if (rookMove(board, myPosition, col, answers, color, i)) {
                    break;
                }
            }
        }
        if (col < 8) { //Moves right
            for (int i = col + 1; i <= 8; i++) {
                if (rookMove(board, myPosition, i, answers, color, row)) {
                    break;
                }
            }
        }
        if (col > 1) { //Moves left
            for (int i = col - 1; i > 0; i--) {
                if (rookMove(board, myPosition, i, answers, color, row)) {
                    break;
                }
            }
        }
    }

    private static boolean rookMove(ChessBoard board, ChessPosition pos, int col, Collection<ChessMove> answers, ChessGame.TeamColor color, int i) {
        if (board.getPiece(i, col) == null) {
            ChessPosition spot = new ChessPosition(i, col);
            ChessMove answer = new ChessMove(pos, spot, null);
            answers.add(answer);
        } else if (board.getPiece(i, col).getTeamColor() != color) {
            ChessPosition spot = new ChessPosition(i, col);
            ChessMove answer = new ChessMove(pos, spot, null);
            answers.add(answer);
            return true;
        } else if (board.getPiece(i, col).getTeamColor() == color) {
            return true;
        }
        return false;
    }

    private static void pawn(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, int row, int col, Collection<ChessMove> answers) {
        if (color == ChessGame.TeamColor.WHITE) { //WHITE MOVES UP
            if (row == 2 && board.getPiece(row + 1, col) == null && board.getPiece(row + 2, col) == null) {
                ChessPosition spot = new ChessPosition(row + 2, col);
                if (row +2==8) {
                    addAll(myPosition, spot, answers);
                }
                else{
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (row < 8) {
                if (board.getPiece(row + 1, col) == null) {
                    ChessPosition spot = new ChessPosition(row + 1, col);
                    if (row +1==8) {
                        addAll(myPosition, spot, answers);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
                if (col < 8 && board.getPiece(row + 1, col + 1) != null && board.getPiece(row + 1, col + 1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(row + 1, col + 1);
                    if (row +1==8) {
                        addAll(myPosition, spot, answers);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
                if (col > 1 && board.getPiece(row + 1, col - 1) != null && board.getPiece(row + 1, col - 1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(row + 1, col - 1);
                    if (row +1==8) {
                        addAll(myPosition, spot, answers);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
            }
        }
        else {
            if (row == 7 && board.getPiece(row - 1, col) == null && board.getPiece(row - 2, col) == null) {
                ChessPosition spot = new ChessPosition(row - 2, col);
                if (row -2==1) {
                    addAll(myPosition, spot, answers);
                }
                else{
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (row > 1) {
                if (board.getPiece(row - 1, col) == null) {
                    ChessPosition spot = new ChessPosition(row - 1, col);
                    if (row -1==1) {
                        addAll(myPosition, spot, answers);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
                if (col < 8 && board.getPiece(row - 1, col + 1) != null && board.getPiece(row - 1, col + 1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(row - 1, col + 1);
                    if (row -1==1) {
                        addAll(myPosition, spot, answers);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
                if (col > 1 && board.getPiece(row - 1, col - 1) != null && board.getPiece(row - 1, col - 1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(row - 1, col - 1);
                    if (row -1==1) {
                        addAll(myPosition, spot, answers);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
            }
        }
    }

    private static void addAll(ChessPosition myPosition, ChessPosition spot, Collection<ChessMove> answers) {
        ChessMove answer = new ChessMove(myPosition, spot, PieceType.QUEEN);
        answers.add(answer);
        answer = new ChessMove(myPosition, spot, PieceType.BISHOP);
        answers.add(answer);
        answer = new ChessMove(myPosition, spot, PieceType.KNIGHT);
        answers.add(answer);
        answer = new ChessMove(myPosition, spot, PieceType.ROOK);
        answers.add(answer);
    }
}
