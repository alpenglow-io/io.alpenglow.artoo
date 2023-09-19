package re.artoo.lance.query;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.one.*;

@FunctionalInterface
public interface One<T> extends Projectable<T>, Triggerable<T>, Filterable<T>, Exceptionable<T>, Coalesceable<T>, Queryable<T> {
  static <ELEMENT> One<ELEMENT> of(final ELEMENT element) {
    return element != null ? () -> Cursor.open(element) : Cursor::empty;
  }

  static <ELEMENT> One<ELEMENT> from(final TrySupplier1<ELEMENT> supplier) {
    return () -> Cursor.lazyValue(supplier);
  }

  static <ELEMENT> One<ELEMENT> gone(final String message, final TryFunction1<? super String, ? extends RuntimeException> error) {
    return () -> {
      try {
        return Cursor.fail(error.invoke(message));
      } catch (Throwable throwable) {
        return Cursor.fail(throwable);
      }
    };
  }

  static <ELEMENT> One<ELEMENT> none() {
    return Cursor::empty;
  }

  default T yield() {
    for (var value : this) return value;
    throw new IllegalStateException("Can't yield for any value, no value is queryable");
  }
}

