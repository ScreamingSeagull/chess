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
        return DAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        //500=description of error
    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData data = DAO.getUser(loginRequest.username());
        if(data.password() != loginRequest.username()){
            throw new ServiceException(401, "Error: unauthorized"); //401=unauthorized
        }
        AuthData authData = new AuthData(generateToken(), loginRequest.username());
        DAOA.createAuth(authData);
        return new LoginResult(authData.username(), authData.authToken());
        //500=description of error
    }
    public void logout(String authToken) throws DataAccessException {
        AuthData authData = DAOA.getAuth(authToken);
        if (authData != null){DAOA.deleteAuth(authData);}
        else {throw new ServiceException(401, "Error: unauthorized");} //unauthorized exception can occur if authdata is not found
        //500=description of error
    }
}
