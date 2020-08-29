package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

public interface Otherwise<T> extends Next<T> {
  @SuppressWarnings("unchecked")
  default Cursor<T> or(final T... values) {
    return or(Cursor.every(values));
  }

  default Cursor<T> or(final Cursor<T> otherwise) {
    return hasNext() && this instanceof Cursor<T> cursor ? cursor : otherwise;
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    if (hasNext() && this instanceof Cursor<T> cursor) {
      return cursor;
    } else {
      throw exception.apply(message);
    }
  }

  default <E extends RuntimeException> Cursor<T> or(final Suppl.Uni<? extends E> exception) {
    return or("nothing", it -> exception.tryGet());
  }
}
