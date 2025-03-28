package Client;

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

public class ServerFacade {
    private final String domainName;
    HttpURLConnection http;
    public ServerFacade(String domainName) throws URISyntaxException, IOException {
        this.domainName = domainName;
    }
    private<T> T makeRequest(String method, String path, Object request, Class<T> responseClass){
        try{
            URI uri = new URI(domainName);
            http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            //throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if(request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try(OutputStream reqBody = http.getOutputStream()){
                reqBody.write(reqData.getBytes());
            } catch (Exception e) {
                throw new ResponseException(500, e.getMessage());
            }
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) {
        T response = null;
        if (http.getContentLength() < 1) {
            try (InputStream resBody = http.getInputStream()) {
                InputStreamReader read = new InputStreamReader(resBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(read, responseClass);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }
    public void clearDB() {
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }
    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, req, RegisterResult.class);
    }
    public LoginResult login(LoginRequest req) {
        var path = "/session";
        return makeRequest("POST", path, req, LoginResult.class);
    }
    public void logout() {
        var path = "/session";
        makeRequest("DELETE", path, null, null);
    }
    public ListGamesResult list() {
        var path = "/game";
        return makeRequest("GET", path, null, ListGamesResult.class);
    }
    public CreateGameResult create(CreateGameRequest req) {
        var path = "/game";
        return makeRequest("POST", path, req, CreateGameResult.class);
    }
    public void join(JoinGameRequest req) {
        var path = "/game";
        makeRequest("PUT", path, req, null);
    }
}
