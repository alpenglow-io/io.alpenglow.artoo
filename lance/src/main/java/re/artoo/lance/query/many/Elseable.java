package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

public interface Elseable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> coalesce(final T... values) {
    return () -> cursor().or(() -> Cursor.open(values));
  }

  default Many<T> coalesce(TrySupplier1<? extends T[]> values) {
    return () -> cursor().or(() -> Cursor.openLazily(values));
  }

  default Many<T> coalesce(final Many<T> many) {
    return () -> cursor().or(many::cursor);
  }
}

