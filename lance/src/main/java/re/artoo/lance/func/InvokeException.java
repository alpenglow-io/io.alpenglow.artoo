package re.artoo.lance.func;

public final class InvokeException extends RuntimeException {
  public InvokeException(Throwable throwable) {
    super(throwable);
  }
  public InvokeException(String message) {
    super(message);
  }
  public InvokeException(String message, Throwable cause) {
    super(message, cause);
  }
}
