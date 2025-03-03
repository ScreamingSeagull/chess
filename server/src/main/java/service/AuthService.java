package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.*;

public class AuthService {
    private AuthDAO DAOA = new AuthDAO();
    public boolean isAuthed(String authToken) throws DataAccessException {
        AuthData authData = DAOA.getAuth(authToken);
        if (authData != null) {return true;}
        return false;
    }
}
