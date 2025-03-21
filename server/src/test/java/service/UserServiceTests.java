package service;
import dataaccess.MemoryAuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.exception.TestException;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTests {
    private static final MemoryAuthDAO aDAO = new MemoryAuthDAO();
    private static final MemoryUserDAO uDAO = new MemoryUserDAO();
    private static final MemoryGameDAO gDAO = new MemoryGameDAO();

    private static final UserService userService = new UserService(uDAO, aDAO, gDAO);
    private static final AppService appService = new AppService(uDAO, aDAO, gDAO);
    private static final GameService gameService = new GameService(uDAO, aDAO, gDAO);

    @Test
    @DisplayName("Good Register")
    public void goodRegister() throws DataAccessException {
        appService.deleteall();
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
        appService.deleteall();
        RegisterRequest badRequest = new RegisterRequest("NewUser", "", "Email@job.com");
        try {
            assertThrows(ServiceException.class, () ->userService.register(badRequest), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Registered, should not have been able to");
        }
    }

    @Test
    @DisplayName("Good Login")
    public void goodLogin() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        try {
            userService.login(loginRequest);
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot login");
        }
    }
    @Test
    @DisplayName("Bad Login")
    public void badLogin() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "Standard password", "Email@job.com");
        userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), "Nonstandard password");
        try {
            assertThrows(ServiceException.class, () ->userService.login(loginRequest), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Logged in, should not have been able to");
        }
    }

    @Test
    @DisplayName("Good Logout")
    public void goodLogout() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        try {
            userService.logout(result.authToken());
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot logout");
        }
    }
    @Test
    @DisplayName("Bad Logout")
    public void badLogout() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "Standard password", "Email@job.com");
        userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        try {
            assertThrows(ServiceException.class, () ->userService.logout(""), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Logged out, should not have been able to");
        }
    }
}
