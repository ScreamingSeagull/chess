package model;

public record AuthData(String authToken, String username) {
    String getToken() {return authToken;}
    String getUsername() {return username;}
}