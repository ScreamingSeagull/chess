package websocket.commands;

public class BoardReq extends UserGameCommand{
    public BoardReq(int gameID, String authToken){
        super(CommandType.BOARDREQ, authToken, gameID);
    }
}
