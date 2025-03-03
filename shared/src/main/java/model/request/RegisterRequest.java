package model.request;

public record RegisterRequest(String username, String password, String email) {
    String getUsername(){
        return username;
    }
    String getPassword() {return password;}
    String getEmail() {return email;}
}
