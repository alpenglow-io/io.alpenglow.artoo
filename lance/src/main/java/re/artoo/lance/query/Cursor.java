package re.artoo.lance.query;

import re.artoo.lance.query.cursor.*;
import re.artoo.lance.query.cursor.Appendable;

import java.util.List;

public non-sealed interface Cursor<ELEMENT>
  extends Mappable<ELEMENT>, Reducible<ELEMENT>, Complementable<ELEMENT>, Appendable<ELEMENT>, Returnable<ELEMENT>, Joinable<ELEMENT> {
  @SafeVarargs
  static <T> Cursor<T> open(T... elements) {
    return new Arranged<>(elements);
  }

  static <T> Cursor<T> chain(Head<T> prev, Head<T> next) {
    return new Chain<>(prev, next);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> empty() {
    return (Cursor<T>) Empty.Default;
  }

  static <VALUE> Cursor<VALUE> maybe(VALUE value) {
    return value == null ? empty() : open(value);
  }

  static <T> Cursor<T> from(List<T> collection) {
    return new Iteration<>(collection);
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

