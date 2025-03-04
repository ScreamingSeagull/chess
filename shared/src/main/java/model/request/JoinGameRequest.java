package model.request;

public record JoinGameRequest(int gameID, String playerColor) {
    int getID() {return gameID;}
    String getColor() {return playerColor;}
}
