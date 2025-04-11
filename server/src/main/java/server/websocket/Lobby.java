package server.websocket;

import com.google.gson.Gson;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Lobby {
    private final ConnectionManagement manager = new ConnectionManagement();
    private final int gameID;
    private Connection white;
    private Connection black;
    private Collection<Connection> spectators;
    public Lobby(int gameID) {
        this.gameID = gameID;
    }
    public void notify(String excluded, Notification notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        Collection<Connection> users = new ArrayList<>(spectators);
        users.addAll(List.of(white, black));
        for (var c : users) {
            if (c.session.isOpen()) {
                if(c.username.equals(excluded)){
                    removeList.add(c);
                } else{
                    c.send(notification.toString());
                }
            }
        }
        for (var c : removeList) {
            manager.remove(c.username);
        }
    }
}
