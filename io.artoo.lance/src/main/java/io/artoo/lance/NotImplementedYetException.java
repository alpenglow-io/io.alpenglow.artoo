package io.artoo.lance;

public final class NotImplementedYetException extends RuntimeException {
  public NotImplementedYetException(final String method) {
    super("""
      Method %s is not implemented yet.
      """
      .formatted(method)
    );
  }
}
