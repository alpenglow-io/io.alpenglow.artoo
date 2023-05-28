package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

@SuppressWarnings("unchecked")
public interface Coalesceable<T> extends Queryable<T> {
  default Many<T> coalesce(final T... values) {
    return () -> cursor().or(Cursor.open(values));
  }

  default Many<T> coalesce(final Many<T> many) {
    return () -> cursor().or(many.cursor());
  }

  default <E extends RuntimeException> Many<T> coalesce(final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> Many<T> coalesce(final TrySupplier1<? extends E> exception) {
    return () -> cursor().or(null, (it, throwable) -> exception.invoke());
  }
}

