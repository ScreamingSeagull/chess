package server;
import java.util.Map;

import spark.*;
import dataaccess.*;
import com.google.gson.Gson;
import service.*;
import model.request.*;

public class Server {
    private UserDAO UDAO;
    private AuthDAO ADAO;
    private GameDAO GDAO;
    private UserService userService;
    private AppService appService;
    private AuthService authService;
    private GameService gameService;
    public int run(int desiredPort) {
        try {
            UDAO = new SQLUserDAO();
            ADAO = new SQLAuthDAO();
            GDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            System.out.println("Server cannot start" + e.getMessage());
        }
        userService = new UserService(UDAO, ADAO, GDAO);
        appService = new AppService(UDAO, ADAO, GDAO);
        authService = new AuthService(UDAO, ADAO, GDAO);
        gameService = new GameService(UDAO, ADAO, GDAO);

        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.delete("/db", this::clearApp);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(DataAccessException.class, this::errorHandler);
        Spark.exception(ServiceException.class, this::serviceErrorHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }
    private Object clearApp(Request req, Response res) throws DataAccessException {
        appService.deleteall();
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    private Object register(Request req, Response res) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class); //convert from req to register request
        String body = new Gson().toJson(userService.register(registerRequest));
        res.status(200); //Throw exceptions in Service
        res.body(body);
        return body;
    }
    private Object login(Request req, Response res) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        String body = new Gson().toJson(userService.login(loginRequest));
        res.status(200); //Throw exceptions in Service
        res.body(body);
        return body;
    }
    private Object logout(Request req, Response res) throws DataAccessException {
        userService.logout(req.headers("Authorization"));
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    private Object listGames(Request req, Response res) throws DataAccessException {
        String body = new Gson().toJson(gameService.listGames(req.headers("Authorization")));
        res.status(200); //Throw exceptions in Service
        res.body(body);
        return body;
    }
    private Object createGame(Request req, Response res) throws DataAccessException {
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        String body = new Gson().toJson(gameService.createGame(createGameRequest, req.headers("Authorization")));
        res.status(200);
        res.body(body);
        return body;

    }
    private Object joinGame(Request req, Response res) throws DataAccessException {
        JoinGameRequest joinRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
        gameService.joinGame(joinRequest, req.headers("Authorization"));
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }
    public Object serviceErrorHandler(ServiceException e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("%s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(e.getCode());
        res.body(body);
        return body;
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
