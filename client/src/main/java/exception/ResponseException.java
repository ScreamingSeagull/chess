package exception;

public class ResponseException extends RuntimeException {
  private final int statusCode;
    public ResponseException(int status, String message) {
      super(message);
      statusCode = status;
    }
    public int getCode() {
      return statusCode;
    }
}
