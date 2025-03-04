package dataaccess;
import model.AuthData;
import model.UserData;
import java.util.HashMap;

public class AuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();
    public void clearA() {
        auths.clear();
        //returns 500 if description
    }
    public void createAuth(AuthData data) throws DataAccessException {
        auths.put(data.authToken(), data);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }
    public void deleteAuth(AuthData authData) throws DataAccessException {
        if (auths.get(authData.authToken()) != null) {
            auths.remove(authData.authToken());
        }
    }
}