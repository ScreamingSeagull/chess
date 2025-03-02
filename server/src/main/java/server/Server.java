package server;
import java.util.UUID;
import spark.*;

public class Server {
    //Content that is constant
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

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    private Object clearApp(Request req, Response res) {
        return 200;
        //returns 500 if description
    }
    private Object register(Request req, Response res) {
        return 200; //+ username and authtoken
        //Returns 400 if bad request, returns 304 if already a token, returns 500 if description
    }
    private Object login(Request req, Response res) {
        return 200; //+username and authtoken
        //Returns 401 if unauthorized, returns 500 if description
    }
    private Object logout(Request req, Response res) {
        return 200;
        //Returns 401 if unauthorized, returns 500 if description
    }
    private Object listGames(Request req, Response res) {
        return 200; //+ games list
        //Returns 401 if unauthorized, returns 500 if description
    }
    private Object createGame(Request req, Response res) {
        return 200; //+game ID
        //Returns 400 if bad request, 401 if unauthorized, returns 500 if description
    }
    private Object joinGame(Request req, Response res) {
        return 200;
        //Returns 401 if unauthorized, 403 if already taken, returns 500 if description
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
