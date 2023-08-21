package re.artoo.lance.query;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.cursor.*;
import re.artoo.lance.query.cursor.operation.*;

public non-sealed interface Cursor<ELEMENT> extends Collector<ELEMENT>, Mappator<ELEMENT>, Folderator<ELEMENT>, Reduciator<ELEMENT>, Alternator<ELEMENT>, Appendor<ELEMENT>, Filterator<ELEMENT> {
  @SafeVarargs
  static <T> Cursor<T> open(T... elements) {
    return new Open<>(elements);
  }

  static <T> Cursor<T> lazyValues(TrySupplier1<? extends T[]> elements) {
    return new LazyValues<>(elements);
  }

  static <T> Cursor<T> lazyValue(TrySupplier1<? extends T> element) {
    return new LazyValue<>(element);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> empty() {
    return (Cursor<T>) Empty.Default;
  }

  static <VALUE> Cursor<VALUE> maybe(VALUE value) {
    return value == null ? empty() : open(value);
  }

  static <T> Cursor<T> from(java.lang.Iterable<T> elements) {
    return new Iterate<>(elements.iterator());
  }

  static <ELEMENT> Cursor<ELEMENT> cause(Throwable throwable) { return new Cause<>(throwable); }

  static Throwable exception(String message, Throwable cause) {
    return new Exception(message, cause);
  }

  static Throwable exception(Throwable cause) {
    return new Exception(cause);
  }

  static Throwable exception(String message) {
    return new Exception(message);
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

