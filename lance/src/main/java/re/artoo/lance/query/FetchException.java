package re.artoo.lance.query;

public final class FetchException extends RuntimeException {
  public static FetchException with(Throwable throwable) {
    return new FetchException(throwable);
  }
  public static FetchException withMessage(String message) {
    return new FetchException(message);
  }

  public static FetchException of(String message, Throwable throwable) {
    return new FetchException(message, throwable);
  }

  public static <RETURN> RETURN byThrowing(Throwable throwable) {
    throw new FetchException(throwable);
  }

  public static <RETURN> RETURN byThrowing(String message, Throwable throwable) {
    throw new FetchException(message, throwable);
  }

  public static <RETURN> RETURN byThrowing(String message) {
    throw new FetchException(message);
  }

  public static <RETURN> RETURN of(String operation, String adjective) {
    throw new FetchException("Can't fetch next element for %s cursor (no more %s elements?)".formatted(operation, adjective));
  }

  private FetchException(final Throwable throwable) {
    super(throwable);
  }

  private FetchException(final String s, final Throwable object) {
    super(s, object);
  }

  private FetchException(String message) { super(message); }
}
