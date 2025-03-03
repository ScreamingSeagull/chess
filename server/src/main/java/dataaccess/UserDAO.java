package dataaccess;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;

import java.util.UUID;

public class UserDAO {
    private AuthDAO DAOA = new AuthDAO();
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public void clearU() throws DataAccessException {
        //returns 200 if all good
        //returns 500 if description
    }
    public RegisterResult createUser(UserData u) throws DataAccessException {
        AuthData auth = new AuthData(generateToken(), u.username());
        DAOA.createAuth(auth);
        //Now add user and auth data to databases
        return new RegisterResult(auth.authToken(), auth.username());
    }
    public UserData getUser(String username) throws DataAccessException {

    }
}