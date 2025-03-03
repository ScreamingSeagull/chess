package model.request;

public record LoginRequest(String username, String password) {
    String getUsername(){
        return username;
    }
    String getPassword() {return password;}
}
