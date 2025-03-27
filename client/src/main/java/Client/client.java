package Client;

import exception.ResponseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class client {
    private final ServerFacade serverFacade;
    public client(String domainName) throws URISyntaxException, IOException {
        serverFacade = new ServerFacade(domainName);
    }
    public void eval(String input) {
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
        if (params.length == 3){

        }
        throw new ResponseException(params.length, "Incorrect number of parameters provided");
    }
    public void login(String... params) {

    }
    public void logout(){

    }
    public void list(){

    }
    public void create(String... params){

    }
    public void join(String... params){

    }
    public void help(){

    }
    private void showGame() {
        //output chessboard
    }
}
