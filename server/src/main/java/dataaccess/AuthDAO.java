package dataaccess;
import model.AuthData;
import model.UserData;
import java.util.HashMap;

public class AuthDAO {
    final private HashMap<Integer, AuthData> auths = new HashMap<>();
    public void clearA() throws DataAccessException {
        auths.clear();
        //returns 500 if description
    }
    public void createAuth(AuthData data) throws DataAccessException {
        auths.put(auths.size(), data);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (int i = 0; i<auths.size(); i++) {
            if(auths.get(i).authToken().equals(authToken)) {
                return auths.get(i);
            }
        }
        return null;
    }
    public void deleteAuth(AuthData authData) throws DataAccessException {
        for (int i = 0; i<auths.size(); i++) {
            if (auths.get(i) == authData) {
                auths.remove(i);
            }
        }
    }
}