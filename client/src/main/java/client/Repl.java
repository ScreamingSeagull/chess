package client;

import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.Notification;
import com.sun.nio.sctp.NotificationHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import static ui.EscapeSequences.*;



public class Repl implements NotificationHandler {
    private final Client command;
    public Repl(String domainName, String serverUrl) throws URISyntaxException, IOException {
        command = new Client(domainName, serverUrl, this);
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

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.toString()); //Originally meant to be message, not toString
        command.prompt();
    }

    @Override
    public HandlerResult handleNotification(Notification notification, Object attachment) {
        return null;
    }
}
