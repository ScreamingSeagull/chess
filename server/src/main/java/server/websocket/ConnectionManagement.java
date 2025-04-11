package server.websocket;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManagement {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public void add (Connection connection) {
        connections.put(connection.username, connection);
    }
    public void remove(String username){
        connections.remove(username);
    }
    public void notify(String exclusion, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if(!c.username.equals(exclusion)){
                    c.send(new Gson().toJson(message));
                }
            } else{
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}
