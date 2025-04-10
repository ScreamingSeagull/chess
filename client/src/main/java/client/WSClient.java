package client;

import com.sun.nio.sctp.NotificationHandler;
import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class WSClient extends Endpoint {
    private final String serverUrl;
    private final NotificationHandler notifHandler;
    private Session session;
    public WSClient(String serverUrl, NotificationHandler notificationHandler) throws DeploymentException, IOException, URISyntaxException {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = (Session) container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>);

        var ws = new WSClient();
        this.serverUrl = serverUrl;
        this.notifHandler = notificationHandler;
        Spark.webSocket("/ws", Client.class);
    }
    public void onMessage(Session session, String message) throws IOException {
        System.out.printf("received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }
    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {

    }
}