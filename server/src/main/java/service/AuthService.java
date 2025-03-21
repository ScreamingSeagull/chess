package service;
import dataaccess.*;

public class AuthService {
    private UserDAO UDAO;
    private AuthDAO ADAO;
    private GameDAO GDAO;

    public AuthService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        UDAO = udao;
        ADAO = adao;
        GDAO = gdao;
    }
//No longer needed, easier to do elsewhere.
//    public boolean isAuthed(String authToken) throws DataAccessException {
//        AuthData authData = DAOA.getAuth(authToken);
//        if (authData != null) {return true;}
//        return false;
//    }
}
