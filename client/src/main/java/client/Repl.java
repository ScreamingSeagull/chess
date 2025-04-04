package client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import static ui.EscapeSequences.*;



public class Repl {
    private final Client command;
    public Repl(String domainName) throws URISyntaxException, IOException {
        command = new Client(domainName);
    }
    public void run() {
        System.out.println("Welcome to CS240 Chess");
        System.out.print(SET_TEXT_COLOR_GREEN);
        command.eval("help");
        Scanner scan = new Scanner(System.in);
        String line = "";
        while (!line.equals("quit")) {
            prompt();
            line = scan.nextLine();
            command.eval(line); //Set eval to return string if needed for command line replies
        }
        System.out.println();
    }
    private void prompt() {
        command.prompt();
    }
}
