package io.artoo.lance.scope;

final class ScopeException extends RuntimeException {
  ScopeException() {
    super();
  }

  ScopeException(final String message) {
    super(message);
  }

  ScopeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  ScopeException(final Throwable cause) {
    super(cause);
  }

  ScopeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
