package artoo.query;

import artoo.cursor.Cursor;
import artoo.func.Cons;

@FunctionalInterface
public interface Queryable<T> extends Iterable<T> {
  default void eventually(final Cons<T> eventually) {
    for (final var value : this)
      eventually.accept(value);
  }

  default Cursor<T> cursor() {
    return (Cursor<T>) this.iterator();
  }
}
