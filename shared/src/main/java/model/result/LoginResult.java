package model.result;

public record LoginResult(String authToken, String username) {
    String getToken() {return authToken;}
    String getUsername() {return username;}
}
