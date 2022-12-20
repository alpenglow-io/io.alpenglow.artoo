package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.query.cursor.*;

import java.util.Iterator;

public non-sealed interface Cursor<T> extends Projector<T>, Replacer<T>, Closer<T>, Convertor<T> {
  @SafeVarargs
  static <T> Cursor<T> open(final T... elements) {
    return new Open<>(elements);
  }

  static <T> Cursor<T> chain(final Fetcher<T> prev, final Fetcher<T> next) {
    return new Chain<>(prev, next);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> empty() {
    return (Cursor<T>) Empty.Default;
  }

  static <VALUE> Cursor<VALUE> maybe(final VALUE value) {
    return value == null ? empty() : open(value);
  }

  static <T> Cursor<T> iteration(final Iterator<T> iterator) {
    return new Iteration<>(iterator);
  }

  static Throwable exception(String message, Throwable cause) { return new Exception(message, cause); }
  static Throwable exception(Throwable cause) { return new Exception(cause); }
  static Throwable exception(String message) { return new Exception(message); }
  final class Exception extends RuntimeException {
    public Exception() {
      super();
    }

    public Exception(String message) {
      super(message);
    }

    public Exception(String message, Throwable cause) {
      super(message, cause);
    }

    public Exception(Throwable cause) {
      super(cause);
    }
  }
}

