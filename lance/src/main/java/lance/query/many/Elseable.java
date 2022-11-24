package lance.query.many;

import lance.func.Func;
import lance.func.Suppl;
import lance.literator.Cursor;
import lance.query.Many;
import lance.Queryable;

@SuppressWarnings("unchecked")
public interface Elseable<T> extends Queryable<T> {
  default Many<T> or(final T... values) {
    return () -> cursor().or(() -> Cursor.open(values));
  }

  default Many<T> or(final Many<T> many) {
    return () -> cursor().or(many::cursor);
  }

  default <E extends RuntimeException> Many<T> or(final String message, final Func.TryBiFunction<? super String, ? super Throwable, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> Many<T> or(final Suppl.MaybeSupplier<? extends E> exception) {
    return () -> cursor().or(null, (it, throwable) -> exception.tryGet());
  }
}

