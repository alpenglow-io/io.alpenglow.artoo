package lance.query.one.oftwo;

import lance.func.Func;
import lance.func.Suppl;
import lance.literator.Cursor;
import lance.query.One;
import lance.Queryable;
import lance.tuple.Pair;

public interface Otherwise<A, B> extends Queryable.OfTwo<A, B> {
  @SuppressWarnings("unchecked")
  default <P extends Pair<A, B>> One.OfTwo<A, B> or(final P... values) {
    return () -> cursor().or(() -> Cursor.open(values));
  }

  default One.OfTwo<A, B> or(final One.OfTwo<A, B> many) {
    return () -> cursor().or(many::cursor);
  }

  default <E extends RuntimeException> One.OfTwo<A, B> or(final String message, final Func.MaybeBiFunction<? super String, ? super Throwable, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> One.OfTwo<A, B> or(final Suppl.MaybeSupplier<? extends E> exception) {
    return () -> cursor().or(null, (it, throwable) -> exception.tryGet());
  }
}

