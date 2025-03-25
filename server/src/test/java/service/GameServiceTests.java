package service;
import dataaccess.*;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.exception.TestException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTests {
    private static SQLAuthDAO aDAO;
    private static SQLUserDAO uDAO;
    private static SQLGameDAO gDAO;

    private static final UserService userService = new UserService(uDAO, aDAO, gDAO);
    private static final AppService appService = new AppService(uDAO, aDAO, gDAO);
    private static final GameService gameService = new GameService(uDAO, aDAO, gDAO);

    public GameServiceTests() {
        try {
            uDAO = new SQLUserDAO();
            aDAO = new SQLAuthDAO();
            gDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            System.out.println("Server cannot start" + e.getMessage());
        }
    }

    @Test
    @DisplayName("Good listGames")
    public void goodListGames() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        try {
            gameService.listGames(result.authToken());
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot display games");
        }
    }
    @Test
    @DisplayName("Bad listGames")
    public void badListGames() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        try {
            assertThrows(ServiceException.class, () ->gameService.listGames(result.username()), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Displayed list of games, should not have been able to");
        }
    }
    @Test
    @DisplayName("Good createGame")
    public void goodCreateGame() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        CreateGameRequest newGameRequest = new CreateGameRequest(result.authToken(), "Game Name No. 1");
        try {
            gameService.createGame(newGameRequest, result.authToken());
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot create game");
        }
    }
    @Test
    @DisplayName("Bad createGame")
    public void badCreateGame() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        CreateGameRequest newGameRequest = new CreateGameRequest(result.authToken(), "Game Name No. 1");
        try {
            assertThrows(ServiceException.class, () ->gameService.createGame(newGameRequest, "authToken"), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Created game, should not have been able to");
        }
    }
    @Test
    @DisplayName("Good joinGame")
    public void goodJoinGame() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        CreateGameRequest newGameRequest = new CreateGameRequest(result.authToken(), "Game Name No. 1");
        gameService.createGame(newGameRequest, result.authToken());
        JoinGameRequest joinRequest = new JoinGameRequest(1, "WHITE");
        try {
            gameService.joinGame(joinRequest, result.authToken());
        } catch (Exception cannotRegister) {
            throw new TestException("Cannot join game");
        }
    }
    @Test
    @DisplayName("Bad joinGame")
    public void badJoinGame() throws DataAccessException {
        appService.deleteall();
        RegisterRequest newRequest = new RegisterRequest("NewUser", "UserPassword", "Email@job.com");
        RegisterResult result = userService.register(newRequest);
        LoginRequest loginRequest = new LoginRequest(newRequest.username(), newRequest.password());
        userService.login(loginRequest);
        CreateGameRequest newGameRequest = new CreateGameRequest(result.authToken(), "Game Name No. 1");
        gameService.createGame(newGameRequest, result.authToken());
        JoinGameRequest joinRequest = new JoinGameRequest(0, "WHITE");
        try {
            assertThrows(ServiceException.class, () ->gameService.joinGame(joinRequest, result.authToken()), "Did not throw ServiceException Error");
        } catch (Exception falseRegister) {
            throw new TestException("Joined game, should not have been able to");
        }
    }
}