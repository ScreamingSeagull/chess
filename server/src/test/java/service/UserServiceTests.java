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
    private static final MemoryAuthDAO ADAO = new MemoryAuthDAO();
    private static final MemoryUserDAO UDAO = new MemoryUserDAO();
    private static final MemoryGameDAO GDAO = new MemoryGameDAO();

    private static final UserService USER_SERVICE = new UserService(UDAO, ADAO, GDAO);
    private static final AppService APP_SERVICE = new AppService(UDAO, ADAO, GDAO);

    @Test
    @DisplayName("Good Register")
    public void goodRegister() throws DataAccessException {
        APP_SERVICE.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        try {
            USER_SERVICE.register(newRequest);
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot register");
        }
    }
    @Test
    @DisplayName("Bad Register")
    public void badRegister() throws DataAccessException {
        APP_SERVICE.deleteall();
        RegisterRequest badRequest = new RegisterRequest("NewUser", "", "Email@job.com");
        try {
            assertThrows(ServiceException.class, () ->USER_SERVICE.register(badRequest), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Registered, should not have been able to");
        }
    }

    @Test
    @DisplayName("Good Login")
    public void goodLogin() throws DataAccessException {
        APP_SERVICE.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        USER_SERVICE.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        try {
            USER_SERVICE.login(loginRequest);
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot login");
        }
    }
    @Test
    @DisplayName("Bad Login")
    public void badLogin() throws DataAccessException {
        APP_SERVICE.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "Standard password", "Email@job.com");
        USER_SERVICE.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), "Nonstandard password");
        try {
            assertThrows(ServiceException.class, () ->USER_SERVICE.login(loginRequest), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Logged in, should not have been able to");
        }
    }

    @Test
    @DisplayName("Good Logout")
    public void goodLogout() throws DataAccessException {
        APP_SERVICE.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = USER_SERVICE.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        USER_SERVICE.login(loginRequest);
        try {
            USER_SERVICE.logout(result.authToken());
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot logout");
        }
    }
    @Test
    @DisplayName("Bad Logout")
    public void badLogout() throws DataAccessException {
        APP_SERVICE.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "Standard password", "Email@job.com");
        USER_SERVICE.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        USER_SERVICE.login(loginRequest);
        try {
            assertThrows(ServiceException.class, () ->USER_SERVICE.logout(""), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Logged out, should not have been able to");
        }
    }
}
