package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.request.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.exception.TestException;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTests {
    private static final AuthDAO aDAO = new AuthDAO();
    private static final UserDAO uDAO = new UserDAO();
    private static final GameDAO gDAO = new GameDAO();

    private static final UserService userService = new UserService(uDAO, aDAO, gDAO);
    private static final AppService appService = new AppService(uDAO, aDAO, gDAO);
    private static final GameService gameService = new GameService(uDAO, aDAO, gDAO);

    @Test
    @DisplayName("Good Register")
    public void goodRegister() throws DataAccessException {
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        try {
            userService.register(newRequest);
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot register");
        }
    }
    @Test
    @DisplayName("Bad Register")
    public void badRegister() throws DataAccessException {
        RegisterRequest badRequest = new RegisterRequest("NewUser", "", "Email@job.com");
        try {
            assertThrows(ServiceException.class, () ->userService.register(badRequest));
        } catch (Exception falseRegister) {
            throw new TestException("Registered, should not have been able to");
        }
    }

    //Positive Login

    //Negative Login

    //Positive Logout

    //Negative Logout
}
