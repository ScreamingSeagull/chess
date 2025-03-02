public record UserData(String username, String password, String email) {
    String getUsername(){
        return username;
    }
    String getPassword() {return password;}
    String getEmail() {return email;}
    UserData resetPassword(String newpass) { //Fun little bonus reset password method (because I have worked in tech support and this is crucial)
        return new UserData((username), newpass, email);
    }
}