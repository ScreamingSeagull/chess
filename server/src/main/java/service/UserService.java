package service;
import dataaccess.DataAccessException;
import model.*;
import dataaccess.*;
import model.result.*;
import model.request.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private UserDAO udao; //originally set to new userDAO, not needed
    private AuthDAO adao;
    private GameDAO gdao;

    public UserService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        this.udao = udao;
        this.adao = adao;
        this.gdao = gdao;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, ServiceException { //400=bad request=DataAccessException
        UserData udata = udao.getUser(registerRequest.username());
        if (udata != null) {
            throw new ServiceException(403, "Error: already taken"); //403=already taken
        }
        if (registerRequest.username()==null || registerRequest.password() == null || registerRequest.email()==null) {
            throw new ServiceException(400, "Error: bad request");
        }
        if (registerRequest.username().isEmpty() || registerRequest.password().isEmpty() || registerRequest.email().isEmpty()) {
            throw new ServiceException(400, "Error: bad request");
        }
        AuthData authData = new AuthData(generateToken(), registerRequest.username());
        adao.createAuth(authData);
        String hash = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        return udao.createUser((new UserData(registerRequest.username(), hash, registerRequest.email())), authData);
        //500=description of error
    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, ServiceException {
        UserData data = udao.getUser(loginRequest.username());
        if (data == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        if (BCrypt.checkpw(loginRequest.password(), data.password())){
            AuthData authData = new AuthData(generateToken(), loginRequest.username());
            adao.createAuth(authData);
            return new LoginResult(authData.authToken(), authData.username());
        }
        else {
            throw new ServiceException(401, "Error: unauthorized"); //401=unauthorized
        }
        //500=description of error
    }
    public void logout(String authToken) throws DataAccessException {
        AuthData authData = adao.getAuth(authToken);
        if (authData != null){
            adao.deleteAuth(authData);}
        else {throw new ServiceException(401, "Error: unauthorized");} //unauthorized exception can occur if authdata is not found
        //500=description of error
    }
}
