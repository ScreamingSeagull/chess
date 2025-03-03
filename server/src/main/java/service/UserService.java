package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.*;
import model.result.*;
import model.request.*;
import dataaccess.UserDAO;

import java.security.Provider;
import java.util.UUID;

public class UserService {
    private UserDAO DAO = new UserDAO();
    private AuthDAO DAOA = new AuthDAO();
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, ServiceException { //400=bad request=DataAccessException
        UserData udata = DAO.getUser(registerRequest.username());
        if (udata != null) {
            throw new ServiceException(403, "Error: already taken"); //403=already taken
        }
        DAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        AuthData auth = new AuthData(generateToken(), registerRequest.username());
        DAOA.createAuth(auth);
        return new RegisterResult(auth.username(), auth.authToken());
        //500=description of error
    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData data = DAO.getUser(loginRequest.username());
        if(data.password() != loginRequest.username()){
            throw new ServiceException(401, "Error: unauthorized");
        }

        //returns 500 if description
    }
    public void logout() {
        //return 200;
        //Returns 401 if unauthorized, returns 500 if description
    }
}
