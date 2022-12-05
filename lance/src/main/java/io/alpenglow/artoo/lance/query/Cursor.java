package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.query.cursor.Closable;
import io.alpenglow.artoo.lance.query.cursor.Iteration;
import io.alpenglow.artoo.lance.query.cursor.Link;
import io.alpenglow.artoo.lance.query.cursor.Mappable;
import io.alpenglow.artoo.lance.query.cursor.Nothing;
import io.alpenglow.artoo.lance.query.cursor.Open;
import io.alpenglow.artoo.lance.query.cursor.Substitutable;
import io.alpenglow.artoo.lance.query.cursor.Transformable;

import java.util.Iterator;

public interface Cursor<T> extends Mappable<T>, Substitutable<T>, Closable<T>, Transformable<T> {
  @SafeVarargs
  static <T> Cursor<T> open(final T... elements) {
    return new Open<>(elements);
  }

  static <T> Cursor<T> link(final Repeatable<T> prev, final Repeatable<T> next) {
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

