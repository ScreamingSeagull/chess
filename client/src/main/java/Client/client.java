package Client;

import exception.ResponseException;
import java.io.IOException;
import java.net.URISyntaxException;
//import client.websocket.WebSocketFacade;
import java.util.Arrays;

public class client {
    private final ServerFacade server;
    //private WebSocketFacade ws;
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

    }
    public void register(String... params) {
        if (params.length == 3){ //Username, password, email
            //ws.register();
            //Return register result
        }
        throw new ResponseException(params.length, "Incorrect number of parameters provided");
    }
    public void login(String... params) {
        if (params.length == 2) { //Username, password
            state = State.SIGNEDIN;
            //ws = new WebSocketFacade(server);
            //ws.login(username, password);
            //Return login result
        }
    }
    public void logout(){

    }
    public void list(){
        //Return listgames result

    }
    public void create(String... params){
        //Return creategames result

    }
    public void join(String... params){

    }
    public void help(){

    }
    private void showGame() {
        //output chessboard
    }
}
