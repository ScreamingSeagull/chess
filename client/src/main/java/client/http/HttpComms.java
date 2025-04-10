package client.http;

import com.google.gson.Gson;
import exception.ResponseException;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.CreateGameResult;
import model.result.ListGamesResult;
import model.result.LoginResult;
import model.result.RegisterResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;

public class HttpComms {
    private final String serverUrl;

    public HttpComms(String serverUrl) {
        this.serverUrl = "http://" + serverUrl;
    }
    public void clearDB() {
        var path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }
    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        RegisterResult res = makeRequest("POST", path, null, req, RegisterResult.class);
        return res;
    }
    public LoginResult login(LoginRequest req) {
        var path = "/session";
        LoginResult res = makeRequest("POST", path, null, req, LoginResult.class);
        return res;
    }
    public void logout(String authToken) {
        var path = "/session";
        makeRequest("DELETE", path, authToken, null, null);
    }
    public ListGamesResult list(String authToken) {
        var path = "/game";
        return makeRequest("GET", path, authToken, null, ListGamesResult.class);
    }
    public CreateGameResult create(String name, String authToken) {
        CreateGameRequest req = new CreateGameRequest(authToken, name);
        var path = "/game";
        return makeRequest("POST", path, authToken, req, CreateGameResult.class);
    }
    public void join(JoinGameRequest req, String authToken) {
        var path = "/game";
        makeRequest("PUT", path, authToken, req, null);
    }
    private<T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException{
        try{
            HttpURLConnection http = (HttpURLConnection) new URI(serverUrl + path).toURL().openConnection();
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
}
