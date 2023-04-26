package re.artoo.lance.query;

public final class OperationException extends RuntimeException {
  public static OperationException with(Throwable throwable) {
    return new OperationException(throwable);
  }
  public static OperationException withMessage(String message) {
    return new OperationException(message);
  }

  public static OperationException of(String message, Throwable throwable) {
    return new OperationException(message, throwable);
  }

  public static <RETURN> RETURN byThrowing(Throwable throwable) {
    throw new OperationException(throwable);
  }

  public static <RETURN> RETURN byThrowing(String message, Throwable throwable) {
    throw new OperationException(message, throwable);
  }

  public static <RETURN> RETURN byThrowing(String message) {
    throw new OperationException(message);
  }

  public static <RETURN> RETURN byThrowingCantFetchNextElement(String operation, String adjective) {
    throw new OperationException("Can't fetch next element for %s cursor (no more %s elements?)".formatted(operation, adjective));
  }

  private OperationException(final Throwable throwable) {
    super(throwable);
  }

  private OperationException(final String s, final Throwable object) {
    super(s, object);
  }

  private OperationException(String message) { super(message); }
}
