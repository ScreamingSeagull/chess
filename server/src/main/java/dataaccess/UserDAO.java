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
    final private HashMap<Integer, UserData> users = new HashMap<>(); //TEMPORARY DATABASE FOR PHASE 3
    public void clearU() throws DataAccessException {
        users.clear();
    }
    public RegisterResult createUser(UserData u, AuthData a) throws DataAccessException {
        users.put(users.size(), u);
        return new RegisterResult(a.authToken(), u.username()); //GET THIS WORKING
    }
    public UserData getUser(String username) throws DataAccessException {
        for (int i = 0; i<users.size(); i++) {
            if(users.get(i).username().equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }
}