package client;

import exception.ResponseException;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() throws Exception{
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void before() throws Exception {
        serverFacade.clearDB();
    }

    @Test
    public void goodRegister() throws ResponseException {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        RegisterResult res = serverFacade.register(req);
        Assertions.assertEquals(res.username(), req.username());
    }
    @Test
    public void badRegister() throws ResponseException {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterRequest req2 = new RegisterRequest("username", "password", null);
        serverFacade.register(req1);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(req2));
    }
    @Test
    public void goodLogin() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        LoginRequest req = new LoginRequest("username", "password");
        LoginResult res = serverFacade.login(req);
        Assertions.assertEquals(req.username(), res.username());
    }
    @Test
    public void badLogin() throws ResponseException {
        LoginRequest req = new LoginRequest("username", "password");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login(req));
    }
    @Test
    public void goodLogout() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.login(new LoginRequest("username", "password"));
        serverFacade.logout();
    }
    @Test
    public void badLogout() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.logout();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout());
    }
    @Test
    public void goodList() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.login(new LoginRequest("username", "password"));
        serverFacade.list();
    }
    @Test
    public void badList() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.logout();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.list());
    }
    @Test
    public void goodCreate() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.login(new LoginRequest("username", "password"));
        serverFacade.create("Test name");
    }
    @Test
    public void badCreate() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.login(new LoginRequest("username", "password"));
        serverFacade.logout();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.create(null));
    }
    @Test
    public void goodJoin() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.login(new LoginRequest("username", "password"));
        serverFacade.create("Test name");
        JoinGameRequest req = new JoinGameRequest(1, "white");
        serverFacade.join(req);
    }
    @Test
    public void badJoin() throws ResponseException {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        serverFacade.login(new LoginRequest("username", "password"));
        serverFacade.create("Test name");
        JoinGameRequest req = new JoinGameRequest(1, "randomcolor");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.join(req));
    }
}
