package dataaccess;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;

import java.util.HashMap;
import java.util.UUID;

public class UserDAO {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    final private HashMap<String, UserData> users = new HashMap<>(); //TEMPORARY DATABASE FOR PHASE 3
    public void clearU() {
        users.clear();
    }
    public RegisterResult createUser(UserData u, AuthData a) throws DataAccessException {
        users.put(u.username(), u);
        return new RegisterResult(a.authToken(), u.username()); //GET THIS WORKING
    }
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }
}