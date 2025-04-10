package client;

import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import javax.websocket.*;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;

public class WSClient extends Endpoint {

    public javax.websocket.Session session;
//    public static void main(String[] args) {
//        Spark.port(8080);
//        Spark.webSocket("/ws", WSClient.class);
//        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//    }
    public WSClient(String serverDomain) throws DeploymentException, IOException, URISyntaxException {
        try {
            URI uri = new URI(String.format("ws://%s/connect", serverDomain));
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });
        } catch (DeploymentException | IOException| URISyntaxException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
//    @OnWebSocketMessage
//    public void onMessage(Session session, String message) throws IOException {
//        System.out.printf("received: %s", message);
//        session.getRemote().sendString("WebSocket response: " + message);
//    }
    public void send(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }
    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
    }
}