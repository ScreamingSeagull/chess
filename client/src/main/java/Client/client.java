package Client;

import exception.ResponseException;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;

import java.io.IOException;
import java.net.URISyntaxException;
//import client.websocket.WebSocketFacade;
import java.util.Arrays;

public class client {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    public client(String domainName) throws URISyntaxException, IOException {
        server = new ServerFacade(domainName);
    }
    public void eval(String input) { //This needs to return data of differing kinds, condense into json?
        String[] line = input.toLowerCase().split(" ");
        String cmd;
        if (line.length == 0) {
            cmd = "help";
        }
        else {
            cmd = line[0];
        }
        String[] params = Arrays.copyOfRange(line, 1, line.length);
        try {
            switch(cmd) {
                case "wipeall"->clearDB();
                case "register"->register(params);
                case "login"->login(params);
                case "logout"->logout();
                case "list"->list();
                case "create"->create(params);
                case "join"->join(params);
                default->help();
            }
        } catch (ResponseException e) {
            throw new ResponseException(999, "Incorrect command entered"); //999 error code because idk what the code is supposed to be
        }
    }
    public void clearDB() {
        server.clearDB();
    }
    public void register(String... params) {
        RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]); //Check parameters
        server.register(req);
    }
    public void login(String... params) {
        LoginRequest req = new LoginRequest(params[0], params[1]); //Check parameters
        server.login(req);
    }
    public void logout(){
        server.logout();
    }
    public void list(){
        server.list();

    }
    public void create(String... params){
        CreateGameRequest req = new CreateGameRequest(params[0], params[1]); //Check parameters
        server.create(req);
    }
    public void join(String... params){
        JoinGameRequest req = new JoinGameRequest(Integer.parseInt(params[0]), params[1]); //Check parameters
        server.join(req);
    }
    public void help(){
        //Not handled by server, keep local
    }
    private void showGame() {
        //output chessboard
    }
}
