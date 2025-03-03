package service;
import dataaccess.DataAccessException;
import model.result.*;
import model.request.*;
import dataaccess.UserDAO;

public class UserService {

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        UserDAO.getUser(registerRequest.username());
        //400=bad request=DataAccessException
        //403=already taken
        //500=description of error
    }
    public LoginResult login(LoginRequest loginRequest) {
        //return 200; //+username and authtoken
        //Returns 401 if unauthorized, returns 500 if description
    }
    public void logout() {
        //return 200;
        //Returns 401 if unauthorized, returns 500 if description
    }
}
