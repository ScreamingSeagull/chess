package model.request;

public record CreateGameRequest(String authToken, String gameName) {
    String getToken() {return authToken;}
}
