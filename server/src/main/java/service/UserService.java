package service;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {}
    public LoginResult login(LoginRequest loginRequest) {
        //return 200; //+username and authtoken
        //Returns 401 if unauthorized, returns 500 if description
    }
    public void logout(LogoutRequest logoutRequest) {
        //return 200;
        //Returns 401 if unauthorized, returns 500 if description
    }
}
