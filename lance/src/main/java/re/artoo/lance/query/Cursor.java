package re.artoo.lance.query;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.cursor.Appendable;
import re.artoo.lance.query.cursor.*;
import re.artoo.lance.query.cursor.operation.*;

public non-sealed interface Cursor<ELEMENT> extends Collectable<ELEMENT>, Mappable<ELEMENT>, Foldable<ELEMENT>, Reducible<ELEMENT>, Alternatively<ELEMENT>, Appendable<ELEMENT>, Filterable<ELEMENT>, Fetchable<ELEMENT> {
  @SuppressWarnings("unchecked")
  @SafeVarargs
  static <ELEMENT> Cursor<ELEMENT> open(ELEMENT... elements) {
    return switch (elements) {
      case ELEMENT[] array when array.length > 1 -> new Open<>(array);
      case ELEMENT[] array when array.length == 1 -> new Open<>(array[0]);
      default -> (Cursor<ELEMENT>) Empty.Default;
    };
  }

  static <ELEMENT> Cursor<ELEMENT> openLazily(TrySupplier1<? extends ELEMENT[]> elements) {
    return new LazyValues<>(elements);
  }

  static <ELEMENT> Cursor<ELEMENT> lazyValue(TrySupplier1<? extends ELEMENT> element) {
    return new LazyValue<>(element);
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Cursor<ELEMENT> empty() {
    return (Cursor<ELEMENT>) Empty.Default;
  }

  static <ELEMENT> Cursor<ELEMENT> maybe(ELEMENT element) {
    return element == null ? empty() : open(element);
  }

  static <ELEMENT> Cursor<ELEMENT> from(Iterable<ELEMENT> elements) {
    return new Iterate<>(elements.iterator());
  }

  static <ELEMENT> Cursor<ELEMENT> fail(Throwable throwable) { return new Throw<>(throwable); }

  final class Exception extends RuntimeException {
    static Throwable of(String message, Throwable cause) {
      return new Exception(message, cause);
    }

    static Throwable of(Throwable cause) {
      return new Exception(cause);
    }

    static Throwable of(String message) {
      return new Exception(message);
    }

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

