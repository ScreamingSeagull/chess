package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ChessPiece {
    ChessGame.TeamColor color;
    PieceType Type;

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

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
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

    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    public PieceType getPieceType() {
        return Type;
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
//PAWN
        if (type == ChessPiece.PieceType.PAWN) {
            if (color == ChessGame.TeamColor.WHITE) { //WHITE MOVES UP
                if (row == 2 && board.getPiece(row + 1, col) == null && board.getPiece(row + 2, col) == null) {
                    ChessPosition spot = new ChessPosition(row + 2, col);
                    if (row+2==8) {
                        ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                        answers.add(answerq);
                        ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                        answers.add(answerb);
                        ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                        answers.add(answerk);
                        ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                        answers.add(answerr);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
                if (row < 8) {
                    if (board.getPiece(row + 1, col) == null) {
                        ChessPosition spot = new ChessPosition(row + 1, col);
                        if (row+1==8) {
                            ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                            answers.add(answerq);
                            ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                            answers.add(answerb);
                            ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                            answers.add(answerk);
                            ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                            answers.add(answerr);
                        }
                        else{
                            ChessMove answer = new ChessMove(myPosition, spot, null);
                            answers.add(answer);
                        }
                    }
                    if (col < 8 && board.getPiece(row + 1, col + 1) != null && board.getPiece(row + 1, col + 1).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row + 1, col + 1);
                        if (row+1==8) {
                            ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                            answers.add(answerq);
                            ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                            answers.add(answerb);
                            ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                            answers.add(answerk);
                            ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                            answers.add(answerr);
                        }
                        else{
                            ChessMove answer = new ChessMove(myPosition, spot, null);
                            answers.add(answer);
                        }
                    }
                    if (col > 1 && board.getPiece(row + 1, col - 1) != null && board.getPiece(row + 1, col - 1).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row + 1, col - 1);
                        if (row+1==8) {
                            ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                            answers.add(answerq);
                            ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                            answers.add(answerb);
                            ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                            answers.add(answerk);
                            ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                            answers.add(answerr);
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
                    if (row-2==1) {
                        ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                        answers.add(answerq);
                        ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                        answers.add(answerb);
                        ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                        answers.add(answerk);
                        ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                        answers.add(answerr);
                    }
                    else{
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    }
                }
                if (row > 1) {
                    if (board.getPiece(row - 1, col) == null) {
                        ChessPosition spot = new ChessPosition(row - 1, col);
                        if (row-1==1) {
                            ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                            answers.add(answerq);
                            ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                            answers.add(answerb);
                            ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                            answers.add(answerk);
                            ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                            answers.add(answerr);
                        }
                        else{
                            ChessMove answer = new ChessMove(myPosition, spot, null);
                            answers.add(answer);
                        }
                    }
                    if (col < 8 && board.getPiece(row - 1, col + 1) != null && board.getPiece(row - 1, col + 1).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row - 1, col + 1);
                        if (row-1==1) {
                            ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                            answers.add(answerq);
                            ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                            answers.add(answerb);
                            ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                            answers.add(answerk);
                            ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                            answers.add(answerr);
                        }
                        else{
                            ChessMove answer = new ChessMove(myPosition, spot, null);
                            answers.add(answer);
                        }
                    }
                    if (col > 1 && board.getPiece(row - 1, col - 1) != null && board.getPiece(row - 1, col - 1).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row - 1, col - 1);
                        if (row-1==1) {
                            ChessMove answerq = new ChessMove(myPosition, spot, PieceType.QUEEN);
                            answers.add(answerq);
                            ChessMove answerb = new ChessMove(myPosition, spot, PieceType.BISHOP);
                            answers.add(answerb);
                            ChessMove answerk = new ChessMove(myPosition, spot, PieceType.KNIGHT);
                            answers.add(answerk);
                            ChessMove answerr = new ChessMove(myPosition, spot, PieceType.ROOK);
                            answers.add(answerr);
                        }
                        else{
                            ChessMove answer = new ChessMove(myPosition, spot, null);
                            answers.add(answer);
                        }
                    }
                }
            }
        }
//ROOK
        if (type == ChessPiece.PieceType.ROOK) {
            if (row < 8) { //Moves up
                for (int i = row + 1; i <= 8; i++) {
                    if (board.getPiece(i, col) == null) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(i, col).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(i, col).getTeamColor() == color) {
                        break;
                    }
                }
            }
            if (row > 1) { //Moves down
                for (int i = row - 1; i >= 1; i--) { //Moves down
                    if (board.getPiece(i, col) == null) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(i, col).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(i, col).getTeamColor() == color) {
                        break;
                    }
                }
            }
            if (col < 8) { //Moves right
                for (int i = col + 1; i <= 8; i++) {
                    if (board.getPiece(row, i) == null) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(row, i).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(row, i).getTeamColor() == color) {
                        break;
                    }
                }
            }
            if (col > 1) {
                for (int i = col - 1; i > 0; i--) { //Moves left
                    if (board.getPiece(row, i) == null) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(row, i).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(row, i).getTeamColor() == color) {
                        break;
                    }
                }
            }
        }
//KNIGHT
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            if (row - 2 >= 1) { //Downwards
                if (col - 1 >= 1 && ((board.getPiece(row - 2, col - 1) == null || board.getPiece(row - 2, col - 1).getTeamColor() != color))) {
                    ChessPosition spot = new ChessPosition(row - 2, col - 1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (col + 1 <= 8 && (board.getPiece(row - 2, col + 1) == null || board.getPiece(row - 2, col + 1).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row - 2, col + 1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (row + 2 <= 8) { //Upwards
                if (col - 1 >= 1 && (board.getPiece(row + 2, col - 1) == null || board.getPiece(row + 2, col - 1).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row + 2, col - 1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (col + 1 <= 8 && (board.getPiece(row + 2, col + 1) == null || board.getPiece(row + 2, col + 1).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row + 2, col + 1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (col + 2 <= 8) { //Right
                if (row - 1 >= 1 && (board.getPiece(row - 1, col + 2) == null || board.getPiece(row - 1, col + 2).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row - 1, col + 2);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (row + 1 <= 8 && (board.getPiece(row + 1, col + 2) == null || board.getPiece(row + 1, col + 2).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row + 1, col + 2);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (col - 2 >= 1) { //Left
                if (row - 1 >= 1 && (board.getPiece(row - 1, col - 2) == null || board.getPiece(row - 1, col - 2).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row - 1, col - 2);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (row + 1 <= 8 && (board.getPiece(row + 1, col - 2) == null || board.getPiece(row + 1, col - 2).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row + 1, col - 2);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
        }
//BISHOP
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP) {
            int temprow = row;
            int tempcol = col;
            while (temprow < 8 && tempcol < 8) { //UP RIGHT
                if(board.getPiece(temprow+1, tempcol+1) == null) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow++;
                    tempcol++;
                }
                else if(board.getPiece(temprow+1,tempcol+1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
            temprow = row;
            tempcol = col;
            while (temprow > 1 && tempcol < 8) { //DOWN RIGHT
                if(board.getPiece(temprow-1, tempcol+1) == null) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow--;
                    tempcol++;
                }
                else if(board.getPiece(temprow-1,tempcol+1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
            temprow = row;
            tempcol = col;
            while (temprow > 1 && tempcol > 1) { //DOWN LEFT
                if(board.getPiece(temprow-1, tempcol-1) == null) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow--;
                    tempcol--;
                }
                else if(board.getPiece(temprow-1,tempcol-1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
            temprow = row;
            tempcol = col;
            while (temprow < 8 && tempcol > 1) { //UP LEFT
                if(board.getPiece(temprow+1, tempcol-1) == null) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow++;
                    tempcol--;
                }
                else if(board.getPiece(temprow+1,tempcol-1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
        }
//QUEEN
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.QUEEN) {
            if (row < 8) { //Moves up
                for (int i = row + 1; i <= 8; i++) {
                    if (board.getPiece(i, col) == null) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(i, col).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(i, col).getTeamColor() == color) {
                        break;
                    }
                }
            }
            if (row > 1) { //Moves down
                for (int i = row - 1; i >= 1; i--) { //Moves down
                    if (board.getPiece(i, col) == null) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(i, col).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(i, col);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(i, col).getTeamColor() == color) {
                        break;
                    }
                }
            }
            if (col < 8) { //Moves right
                for (int i = col + 1; i <= 8; i++) {
                    if (board.getPiece(row, i) == null) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(row, i).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(row, i).getTeamColor() == color) {
                        break;
                    }
                }
            }
            if (col > 1) {
                for (int i = col - 1; i > 0; i--) { //Moves left
                    if (board.getPiece(row, i) == null) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                    } else if (board.getPiece(row, i).getTeamColor() != color) {
                        ChessPosition spot = new ChessPosition(row, i);
                        ChessMove answer = new ChessMove(myPosition, spot, null);
                        answers.add(answer);
                        break;
                    } else if (board.getPiece(row, i).getTeamColor() == color) {
                        break;
                    }
                }
            }
            int temprow = row;
            int tempcol = col;
            while (temprow < 8 && tempcol < 8) { //UP RIGHT
                if(board.getPiece(temprow+1, tempcol+1) == null) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow++;
                    tempcol++;
                }
                else if(board.getPiece(temprow+1,tempcol+1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
            temprow = row;
            tempcol = col;
            while (temprow > 1 && tempcol < 8) { //DOWN RIGHT
                if(board.getPiece(temprow-1, tempcol+1) == null) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow--;
                    tempcol++;
                }
                else if(board.getPiece(temprow-1,tempcol+1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
            temprow = row;
            tempcol = col;
            while (temprow > 1 && tempcol > 1) { //DOWN LEFT
                if(board.getPiece(temprow-1, tempcol-1) == null) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow--;
                    tempcol--;
                }
                else if(board.getPiece(temprow-1,tempcol-1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow-1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
            temprow = row;
            tempcol = col;
            while (temprow < 8 && tempcol > 1) { //UP LEFT
                if(board.getPiece(temprow+1, tempcol-1) == null) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    temprow++;
                    tempcol--;
                }
                else if(board.getPiece(temprow+1,tempcol-1).getTeamColor() != color) {
                    ChessPosition spot = new ChessPosition(temprow+1, tempcol-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                    break;
                }
                else break;
            }
        }
//KING
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING){
            if (row < 8) { //Upwards
                if (board.getPiece(row+1,col) == null || (board.getPiece(row+1,col).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row+1, col);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (col - 1 > 1 && (board.getPiece(row+1,col-1) == null || (board.getPiece(row+1,col-1).getTeamColor() != color))) {
                    ChessPosition spot = new ChessPosition(row+1, col-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (col + 1 < 8 && (board.getPiece(row+1,col+1) == null || (board.getPiece(row+1,col+1).getTeamColor() != color))) {
                    ChessPosition spot = new ChessPosition(row+1, col+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (row > 1) { //Downwards
                if (board.getPiece(row-1,col) == null || (board.getPiece(row-1,col).getTeamColor() != color)) {
                    ChessPosition spot = new ChessPosition(row-1, col);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (col - 1 > 1 && (board.getPiece(row-1,col-1) == null || (board.getPiece(row-1,col-1).getTeamColor() != color))) {
                    ChessPosition spot = new ChessPosition(row-1, col-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
                if (col + 1 < 8 && (board.getPiece(row-1,col+1) == null || (board.getPiece(row-1,col+1).getTeamColor() != color))) {
                    ChessPosition spot = new ChessPosition(row-1, col+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (col > 1) {
                if (board.getPiece(row,col-1) == null || (board.getPiece(row,col-1).getTeamColor() != color)){
                    ChessPosition spot = new ChessPosition(row, col-1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
            if (col < 8) {
                if (board.getPiece(row,col+1) == null || (board.getPiece(row,col+1).getTeamColor() != color)){
                    ChessPosition spot = new ChessPosition(row, col+1);
                    ChessMove answer = new ChessMove(myPosition, spot, null);
                    answers.add(answer);
                }
            }
        }
        return answers;
    }
}
