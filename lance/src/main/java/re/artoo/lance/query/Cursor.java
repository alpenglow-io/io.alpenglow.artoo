package re.artoo.lance.query;

import re.artoo.lance.query.cursor.*;

import java.util.Iterator;

public non-sealed interface Cursor<T> extends Mappable<T>, Reducible<T>, Complementable<T>, Collectable<T>, Returnable<T> {
  @SafeVarargs
  static <T> Cursor<T> open(T... elements) {
    return new Forward<>(elements);
  }

  static <T> Cursor<T> chain(Probe<T> prev, Probe<T> next) {
    return new Chain<>(prev, next);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> empty() {
    return (Cursor<T>) Empty.Default;
  }

  static <VALUE> Cursor<VALUE> maybe(VALUE value) {
    return value == null ? empty() : open(value);
  }

  static <T> Cursor<T> iteration(Iterator<T> iterator) {
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

