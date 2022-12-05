package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.query.cursor.*;

import java.util.Iterator;

public non-sealed interface Cursor<T> extends Mappable<T>, Substitutable<T>, Closable<T>, Transformable<T> {
  @SafeVarargs
  static <T> Cursor<T> open(final T... elements) {
    return new Open<>(elements);
  }

  static <T> Cursor<T> link(final Source<T> prev, final Source<T> next) {
    return new Link<>(prev, next);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> nothing() {
    return (Cursor<T>) Nothing.Default;
  }

  static <T> Cursor<T> maybe(final T element) {
    return element == null ? nothing() : open(element);
  }

  static <T> Cursor<T> iteration(final Iterator<T> iterator) {
    return new Iteration<>(iterator);
  }

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

