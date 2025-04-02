package model;

public record UserData(String username, String password, String email) {
    String getUsername(){
        return username;
    }
    String getPassword() {return password;}
    String getEmail() {return email;}
}