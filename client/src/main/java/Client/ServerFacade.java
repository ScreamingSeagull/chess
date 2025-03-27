package Client;

import exception.ResponseException;
import model.request.*;
import model.result.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {
    private final String domainName;
    HttpURLConnection http;
    public ServerFacade(String domainName) throws URISyntaxException, IOException {
        this.domainName = domainName;
        URI uri = new URI(domainName);
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.connect();
    }
    public void clearDB() {

    }
    public RegisterResult register(RegisterRequest req) throws ResponseException {
        RegisterResult respond = new RegisterResult("authToken default", req.username());
        return respond;
    }
    public LoginResult login(LoginRequest req) {
        LoginResult respond = new LoginResult("authToken default", req.username());
        return respond;
    }
    public void logout(String authToken) {

    }
    public ListGamesResult list() {
        ListGamesResult respond = new ListGamesResult(null);
        return respond;
    }
    public CreateGameResult create(CreateGameRequest req) {
        CreateGameResult respond = new CreateGameResult(1);
        return respond;
    }
    public void join(JoinGameRequest req) {

    }
}
