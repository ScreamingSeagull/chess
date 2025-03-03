package service;

public class ServiceException extends RuntimeException {
  private final int status;
    public ServiceException(int number, String message) {
      super(message);
      this.status = number;
    }
    public int getCode() {
      return status;
    }
}