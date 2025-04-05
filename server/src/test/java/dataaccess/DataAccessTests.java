package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataAccessTests {
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    @BeforeAll
    public static void init() throws Exception{
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            System.out.println("Server cannot start" + e.getMessage());
        }
    }
    @BeforeEach
    public void before() throws DataAccessException {
        authDAO.clearA();
        userDAO.clearU();
        gameDAO.clearG();
    }
    @Test
    public void goodClear() throws DataAccessException {
        authDAO.clearA();
        userDAO.clearU();
        gameDAO.clearG();
        Assertions.assertNull(authDAO.getAuth("user"));
    }
    //Auth DAO
    @Test
    public void goodCreateAuth() throws DataAccessException {
        AuthData data = new AuthData("token", "username");
        authDAO.createAuth(data);
        Assertions.assertEquals(authDAO.getAuth("token"), data);
    }
    @Test
    public void badCreateAuth() {
        AuthData badData = null;
        Assertions.assertThrows(NullPointerException.class, () -> authDAO.createAuth(badData));
    }
    @Test
    public void goodGetAuth() throws DataAccessException {
        AuthData data1 = new AuthData("token1", "username1");
        authDAO.createAuth(data1);
        AuthData data2 = new AuthData("token2", "username2");
        authDAO.createAuth(data2);
        AuthData data3 = new AuthData("token3", "username3");
        authDAO.createAuth(data3);
        Assertions.assertEquals(authDAO.getAuth("token2"), data2);
    }
    @Test
    public void badGetAuth() throws DataAccessException {
        AuthData data = new AuthData("token", "username");
        authDAO.createAuth(data);
        Assertions.assertNull(authDAO.getAuth("incorrect"));
    }
    @Test
    public void goodDeleteAuth() throws DataAccessException {
        AuthData data = new AuthData("token", "username");
        authDAO.createAuth(data);
        authDAO.deleteAuth(data);
        Assertions.assertNull(authDAO.getAuth("token"));
    }
    @Test
    public void badDeleteAuth() throws DataAccessException {
        AuthData data = new AuthData("token", "username");
        authDAO.deleteAuth(data);
        Assertions.assertNull(authDAO.getAuth("token"));
    }
    //User DAO
    @Test
    public void goodCreateUser() throws DataAccessException {
        UserData uData = new UserData("username", "password", "email");
        AuthData aData = new AuthData("token", "username");
        userDAO.createUser(uData, aData);
        Assertions.assertEquals(userDAO.getUser("username"), uData);
    }
    @Test
    public void badCreateUser() throws DataAccessException {
        UserData uData = null;
        AuthData aData = null;
        Assertions.assertThrows(NullPointerException.class, () -> userDAO.createUser(uData, aData));
    }
    @Test
    public void goodGetUser() throws DataAccessException {
        UserData data1 = new UserData("user", "pass", "email");
        AuthData data2 = new AuthData("token", "user");
        userDAO.createUser(data1, data2);
        Assertions.assertEquals(userDAO.getUser("user"), data1);
    }
    @Test
    public void badGetUser() throws DataAccessException {
        UserData data1 = new UserData("username", "pass", "email");
        AuthData data2 = new AuthData("token", "user");
        userDAO.createUser(data1, data2);
        Assertions.assertNull(userDAO.getUser("user"));
    }
    //Game DAO
    @Test
    public void goodCreateGame() throws DataAccessException {
        gameDAO.createGame("Game1");
        Assertions.assertEquals(gameDAO.getGame(1).gameName(), "Game1");
    }
    @Test
    public void badCreateGame() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }
    @Test
    public void goodGetGame() throws DataAccessException {
        gameDAO.createGame("testgame");
        Assertions.assertEquals(gameDAO.getGame(1).gameName(), "testgame");
    }
    @Test
    public void badGetGame() throws DataAccessException {
        Assertions.assertNull( gameDAO.getGame(5));
    }
    @Test
    public void goodListGames() throws DataAccessException {
        gameDAO.createGame("Game1");
        gameDAO.createGame("Game2");
        gameDAO.createGame("Game3");
        gameDAO.listGames();
    }
    @Test
    public void badListGames() throws DataAccessException {
        gameDAO.listGames();
    }
    @Test
    public void goodUpdateGame() throws DataAccessException {
        gameDAO.createGame("Game1");
        GameData newData = new GameData(1, "w user", "b user", "New gamename", new ChessGame());
        gameDAO.updateGame(1, newData);
        Assertions.assertEquals(gameDAO.getGame(1).gameName(), "New gamename");
        Assertions.assertEquals(gameDAO.getGame(1).whiteUsername(), "w user");
        Assertions.assertEquals(gameDAO.getGame(1).blackUsername(), "b user");
    }
    @Test
    public void badUpdateGame() throws DataAccessException {
        gameDAO.createGame("Game1");
        GameData newData = new GameData(1, null, null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(1, newData));
    }
}
