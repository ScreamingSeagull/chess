package client.websocket;

import exception.ResponseException;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    javax.websocket.Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DeploymentException, IOException, URISyntaxException {
        try {
            url = url.replace("http", "ws");
            URI uri = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
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
    public void send(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }
    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
    }
}