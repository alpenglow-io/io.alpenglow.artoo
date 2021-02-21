package io.artoo.lance.literator;

public final class FetchException extends RuntimeException {
  public static FetchException of(Throwable throwable) {
    return new FetchException(throwable);
  }

  public static FetchException of(String message, Throwable throwable) {
    return new FetchException(message, throwable);
  }

  private FetchException(final Throwable throwable) {
    super(throwable);
  }

  private FetchException(final String s, final Throwable object) {
    super(s, object);
  }
}
