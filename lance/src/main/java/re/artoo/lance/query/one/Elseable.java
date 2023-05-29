package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.One;

public interface Elseable<ELEMENT> extends Queryable<ELEMENT> {
  default One<ELEMENT> or(final ELEMENT element) {
    return or(One.value(element));
  }

  default <OTHER extends One<ELEMENT>> One<ELEMENT> or(final OTHER otherwise) {
    return () -> cursor().or(otherwise.cursor());
  }

  default One<ELEMENT> or(final String message, final TryFunction1<? super String, ? extends Throwable> exception) {
    return () -> cursor().or(message, exception);
  }

  default One<ELEMENT> or(final TrySupplier1<? extends Throwable> exception) {
    return or(null, __ -> exception.invoke());
  }

  default ELEMENT otherwise(final ELEMENT other) {
    return or(other).iterator().next();
  }

  default ELEMENT otherwise(final String message, final TryFunction1<? super String, ? extends Throwable> exception) {
    return or(message, exception).iterator().next();
  }

  default ELEMENT otherwise(final TrySupplier1<? extends Throwable> exception) {
    return or(exception).iterator().next();
  }
}
