package artoo.query;

public final class UnwrapException extends RuntimeException {
  public UnwrapException() {
    super();
  }

  public UnwrapException(String message) {
    super(message);
  }

  public UnwrapException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnwrapException(Throwable cause) {
    super(cause);
  }

  protected UnwrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
