package exception;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ResponseException extends RuntimeException {
  private final int statusCode;
    public ResponseException(int status, String message) {
      super(message);
      statusCode = status;
    }
  public static ResponseException fromJson(InputStream stream) {
    var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
    var status = ((Double)map.get("status")).intValue();
    String message = map.get("message").toString();
    return new ResponseException(status, message);
  }
    public int getCode() {
      return statusCode;
    }
}
