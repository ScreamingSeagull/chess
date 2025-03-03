package model.request;

public record JoinGameRequest(int ID, String color) {
    int getID() {return ID;}
    String getColor() {return color;}
}
