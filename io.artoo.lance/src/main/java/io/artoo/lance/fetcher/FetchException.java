package io.artoo.lance.fetcher;

public final class FetchException extends RuntimeException {
  public FetchException(final Throwable throwable) {
    super(throwable);
  }
}
