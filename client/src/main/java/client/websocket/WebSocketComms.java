package client.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import websocket.commands.UserGameCommand;


public class WebSocketComms extends Endpoint {
    Session session;
    public WebSocketComms(String url, MessageHandler.Whole<String> messageHandler) throws ResponseException {
        try {
            URI uri = new URI(String.format("ws://%s/connect", url));
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler(messageHandler);
        } catch (DeploymentException | IOException| URISyntaxException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public void sendMessage(UserGameCommand message) throws ResponseException {
        try {
            session.getBasicRemote().sendText(new Gson().toJson(message));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}