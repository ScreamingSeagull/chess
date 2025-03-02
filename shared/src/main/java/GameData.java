import chess.ChessGame;
public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    int getGameID(){
        return gameID;
    }
    String getWhite() {return whiteUsername;}
    String getBlack() {return blackUsername;}
    String getGameName() {return gameName;}
    ChessGame getGame() {return game;}
}