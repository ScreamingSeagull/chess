package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void clearA() throws DataAccessException;
    public void createAuth(AuthData data) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(AuthData authData) throws DataAccessException;
}
