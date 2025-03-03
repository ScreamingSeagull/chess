package model.request;

public record CreateGameRequest(String authToken) {
    String getToken() {return authToken;}
}
