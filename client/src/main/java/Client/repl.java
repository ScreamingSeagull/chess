package Client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import static ui.EscapeSequences.*;



public class repl {
    private final client command;
    public repl(String domainName) throws URISyntaxException, IOException {
        command = new client(domainName);
    }
    public void run() {
        System.out.println("Welcome to CS240 Chess"); //Set text to new color, yellow perhaps
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
        System.out.println("\n" + RESET_TEXT_COLOR + ">>>" + SET_TEXT_COLOR_GREEN);
    }
}
