package Service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

public class ServiceTests {
    private static TestUser existingUser;
    private static TestUser newUser;
    private String existingAuth;
    private String newAuth;

    public void successLogin() {
        TestAuthResult loginResult = serverFacade.login(existingUser);

        assertHttpOk(loginResult);
        Assertions.assertEquals(existingUser.getUsername(), loginResult.getUsername(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(loginResult.getAuthToken(), "Response did not return authentication String");
    }
}
