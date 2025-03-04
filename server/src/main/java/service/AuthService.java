package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;

public class AuthService {
    private UserDAO UDAO = new UserDAO();
    private AuthDAO ADAO = new AuthDAO();
    private GameDAO GDAO = new GameDAO();

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
