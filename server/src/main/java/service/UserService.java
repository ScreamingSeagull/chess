package service;
import dataaccess.DataAccessException;
import model.*;
import dataaccess.*;
import model.result.*;
import model.request.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private UserDAO UDAO; //originally set to new userDAO, not needed
    private AuthDAO ADAO;
    private GameDAO GDAO;

    public UserService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        UDAO = udao;
        ADAO = adao;
        GDAO = gdao;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, ServiceException { //400=bad request=DataAccessException
        UserData udata = UDAO.getUser(registerRequest.username());
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
        ADAO.createAuth(authData);
        String hash = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        return UDAO.createUser((new UserData(registerRequest.username(), hash, registerRequest.email())), authData);
        //500=description of error
    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, ServiceException {
        UserData data = UDAO.getUser(loginRequest.username());
        if (data == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        if (BCrypt.checkpw(loginRequest.password(), data.password())){
            AuthData authData = new AuthData(generateToken(), loginRequest.username());
            ADAO.createAuth(authData);
            return new LoginResult(authData.authToken(), authData.username());
        }
        else {
            throw new ServiceException(401, "Error: unauthorized"); //401=unauthorized
        }
        //500=description of error
    }
    public void logout(String authToken) throws DataAccessException {
        AuthData authData = ADAO.getAuth(authToken);
        if (authData != null){ADAO.deleteAuth(authData);}
        else {throw new ServiceException(401, "Error: unauthorized");} //unauthorized exception can occur if authdata is not found
        //500=description of error
    }
}
