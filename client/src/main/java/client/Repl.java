package client;

import java.util.Scanner;
import java.io.IOException;
import java.net.URISyntaxException;
import static ui.EscapeSequences.*;



public class Repl {
    private final Client client;
    public Repl(String serverUrl) throws URISyntaxException, IOException {
        client = new Client(serverUrl);
    }
    public void run() {
        System.out.println("Welcome to CS240 Chess");
        System.out.print(SET_TEXT_COLOR_GREEN);
        client.eval("help");
        Scanner scan = new Scanner(System.in);
        String line = "";
        while (!line.equals("quit")) {
            prompt();
            line = scan.nextLine();
            client.eval(line); //Set eval to return string if needed for command line replies
        }
        System.out.println();
    }
    private void prompt() {
        client.prompt();
    }
}
