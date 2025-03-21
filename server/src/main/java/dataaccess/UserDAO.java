package dataaccess;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;

import java.util.HashMap;
import java.util.UUID;

public interface UserDAO {
    public void clearU(); //Does this need to throw a data access exception at all??? ******************************************
    public RegisterResult createUser(UserData u, AuthData a) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
}