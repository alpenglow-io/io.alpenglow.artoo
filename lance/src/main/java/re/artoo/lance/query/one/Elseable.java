package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.One;

public interface Elseable<ELEMENT> extends Queryable<ELEMENT> {
  default One<ELEMENT> or(final ELEMENT element) {
    return or(One.of(element));
  }

  default <OTHER extends One<ELEMENT>> One<ELEMENT> or(final OTHER otherwise) {
    return () -> cursor().or(otherwise::cursor);
  }

  default <E extends RuntimeException> One<ELEMENT> or(final String message, final TryFunction2<? super Throwable, ? super String, ? extends E> exception) {
    return () -> cursor().or(message, (m, c) -> exception.invoke(c, m));
  }

  default <E extends RuntimeException> One<ELEMENT> or(final String message, final TryFunction1<? super String, ? extends E> exception) {
    return () -> cursor().or(message, (msg, cause) -> exception.invoke(msg));
  }

  default <E extends RuntimeException> One<ELEMENT> or(final TrySupplier1<? extends E> exception) {
    return or(null, (it, throwable) -> exception.invoke());
  }

  default ELEMENT otherwise(final ELEMENT other) {
    return or(other).iterator().next();
  }

  default <E extends RuntimeException> ELEMENT otherwise(final String message, final TryFunction2<? super Throwable, ? super String, ? extends E> exception) {
    return or(message, exception).iterator().next();
  }

  default <E extends RuntimeException> ELEMENT otherwise(final TrySupplier1<? extends E> exception) {
    return or(exception).iterator().next();
  }
}
