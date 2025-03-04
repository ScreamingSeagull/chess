package service;
import dataaccess.*;
import model.request.CreateGameRequest;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.exception.TestException;

public class AppServiceTests {
    private static final AuthDAO aDAO = new AuthDAO();
    private static final UserDAO uDAO = new UserDAO();
    private static final GameDAO gDAO = new GameDAO();

    private static final UserService userService = new UserService(uDAO, aDAO, gDAO);
    private static final AppService appService = new AppService(uDAO, aDAO, gDAO);
    private static final GameService gameService = new GameService(uDAO, aDAO, gDAO);

    @Test
    @DisplayName("Clear Database")
    public void clearDatabase() throws DataAccessException {
        RegisterResult registerResult = userService.register(new RegisterRequest("NewUser", "UserPassword", "Email@job.com"));
        gameService.createGame(new CreateGameRequest(registerResult.authToken(),"Basic Chess Game"), registerResult.authToken());
        try {
            appService.deleteall();
        } catch (Exception cannotClear) {
            throw new TestException("cannot clear database");
        }
    }
}
