package service;
import dataaccess.*;
import model.request.CreateGameRequest;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.exception.TestException;

public class AppServiceTests {
    private static final MemoryAuthDAO ADAO = new MemoryAuthDAO();
    private static final MemoryUserDAO UDAO = new MemoryUserDAO();
    private static final MemoryGameDAO GDAO = new MemoryGameDAO();

    private static final UserService USER_SERVICE = new UserService(UDAO, ADAO, GDAO);
    private static final AppService APP_SERVICE = new AppService(UDAO, ADAO, GDAO);
    private static final GameService GAME_SERVICE = new GameService(UDAO, ADAO, GDAO);

    @Test
    @DisplayName("Clear Database")
    public void clearDatabase() throws DataAccessException {
        RegisterResult registerResult = USER_SERVICE.register(new RegisterRequest("NewUser", "UserPassword", "Email@job.com"));
        GAME_SERVICE.createGame(new CreateGameRequest(registerResult.authToken(),"Basic Chess Game"), registerResult.authToken());
        try {
            APP_SERVICE.deleteall();
        } catch (Exception cannotClear) {
            throw new TestException("cannot clear database");
        }
    }
}
