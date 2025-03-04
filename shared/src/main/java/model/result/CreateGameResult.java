package model.result;

public record CreateGameResult(int gameID) {
    int getID() {return gameID;}
}
