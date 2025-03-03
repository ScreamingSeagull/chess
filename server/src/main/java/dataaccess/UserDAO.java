package dataaccess;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;

import java.util.HashMap;
import java.util.UUID;

public class UserDAO {
    private AuthDAO DAOA = new AuthDAO();
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    final private HashMap<Integer, UserData> users = new HashMap<>();
    //TEMPORARY DATABASE FOR PHASE 3
    public void clearU() throws DataAccessException {
        users.clear();
        //returns 500 if description
    }
    public RegisterResult createUser(UserData u) throws DataAccessException {
        users.put(users.size(), u);
        AuthData auth = new AuthData(generateToken(), u.username());
        DAOA.createAuth(auth);
        //Now add user and auth data to databases
        return new RegisterResult(auth.authToken(), auth.username());
    }
    public UserData getUser(String username) throws DataAccessException {
        for (int i = 0; i<users.size(); i++) {
            if(users.get(i).username() == username) {
                return users.get(i);
            }
        }
        return null;
    }
}