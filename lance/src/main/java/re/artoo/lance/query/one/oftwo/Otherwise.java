package re.artoo.lance.query.one.oftwo;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;
import re.artoo.lance.tuple.Pair;

public interface Otherwise<A, B> extends Queryable.OfTwo<A, B> {
  @SuppressWarnings("unchecked")
  default <P extends Pair<A, B>> One.OfTwo<A, B> or(final P... values) {
    return () -> cursor().or(() -> Cursor.open(values));
  }

  default One.OfTwo<A, B> or(final One.OfTwo<A, B> many) {
    return () -> cursor().or(many::cursor);
  }

  default <E extends RuntimeException> One.OfTwo<A, B> or(final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> One.OfTwo<A, B> or(final TrySupplier1<? extends E> exception) {
    return () -> cursor().or(null, (it, throwable) -> exception.invoke());
  }
}

