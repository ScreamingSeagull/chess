package client;

import client.websocket.NotificationHandler;
import com.google.gson.Gson;
import exception.ResponseException;
import model.request.*;
import model.result.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class ServerFacade {
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    String authToken;

    public ServerFacade(String domainName, NotificationHandler notificationHandler) throws URISyntaxException, IOException {
        this.serverUrl = domainName;
        this.notificationHandler = notificationHandler;
    }

    private<T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException{
        try{
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("authorization", authToken);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if(request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            OutputStream reqBody = http.getOutputStream();
            reqBody.write(reqData.getBytes());
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 1) {
            InputStream resBody = http.getInputStream();
            InputStreamReader read = new InputStreamReader(resBody);
            if (responseClass != null) {
                response = new Gson().fromJson(read, responseClass);
            }
        }
        return response;
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    var map = new Gson().fromJson(new InputStreamReader(respErr), HashMap.class);
                    String message = map.get("message").toString();

                    throw new ResponseException(status, message);
                }
            }
            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
    public void clearDB() {
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }
    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        RegisterResult res = makeRequest("POST", path, req, RegisterResult.class);
        authToken = res.authToken();
        return res;
    }
    public LoginResult login(LoginRequest req) {
        var path = "/session";
        LoginResult res = makeRequest("POST", path, req, LoginResult.class);
        authToken = res.authToken();
        return res;
    }
    public void logout() {
        var path = "/session";
        makeRequest("DELETE", path, null, null);
    }
    public ListGamesResult list() {
        var path = "/game";
        return makeRequest("GET", path, null, ListGamesResult.class);
    }
    public CreateGameResult create(String name) {
        CreateGameRequest req = new CreateGameRequest(authToken, name);
        var path = "/game";
        return makeRequest("POST", path, req, CreateGameResult.class);
    }
    public void join(JoinGameRequest req) {
        var path = "/game";
        makeRequest("PUT", path, req, null);
    }
}
