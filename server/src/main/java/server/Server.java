package server;
import java.util.Map;
import java.util.UUID;
import org.eclipse.jetty.server.Authentication;
import spark.*;
import dataaccess.*;
import com.google.gson.Gson;
import service.*;
import model.request.*;
import model.result.*;

public class Server {
    private UserService userService = new UserService();
    private AppService appService = new AppService();
    private AuthService authService = new AuthService();
    private GameService gameService = new GameService();
    public int run(int desiredPort) {

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

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    private Object clearApp(Request req, Response res) throws DataAccessException {
        appService.clear();
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    private Object register(Request req, Response res) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class); //convert from req to register request
        userService.register(registerRequest);
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    private Object login(Request req, Response res) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        userService.login(loginRequest);
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";

    }
    private Object logout(Request req, Response res) throws DataAccessException {
        userService.logout(req.headers("Authorization"));
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    private Object listGames(Request req, Response res) {
        gameService.listGames();
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    private Object createGame(Request req, Response res) throws DataAccessException {
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        gameService.createGame(createGameRequest);
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    private Object joinGame(Request req, Response res) throws DataAccessException {
        JoinGameRequest joinRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
        gameService.joinGame(joinRequest);
        res.status(200); //Throw exceptions in Service
        res.body("");
        return "";
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
