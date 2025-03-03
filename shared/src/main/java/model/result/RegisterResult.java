package model.result;

public record RegisterResult(String authToken, String username) {
    String getToken() {return authToken;}
    String getUsername() {return username;}
}
