package io.artoo.lance.fetcher;

public final class FetchException extends RuntimeException {
  public static FetchException wrapping(Throwable throwable) {
    return new FetchException(throwable);
  }

  public static FetchException withMessage(String message, Throwable throwable) {
    return new FetchException(message, throwable);
  }

  private FetchException(final Throwable throwable) {
    super(throwable);
  }

  private FetchException(final String s, final Throwable object) {
    super(s, object);
  }
}
