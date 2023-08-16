package re.artoo.lance;

import re.artoo.lance.query.Cursor;

import java.util.Iterator;

public interface Queryable<T> extends Iterable<T> {
  Cursor<T> cursor();

  @Override
  default Iterator<T> iterator() {
    try {
      return cursor();
    } catch (Throwable throwable) {
      throw new Cursor.Exception("Can't run cursor, since %s".formatted(throwable.getMessage()), throwable);
    }
  }

  default T fetch() {
    for (final var value : this) return value;
    throw new IllegalStateException("Can't retrieve any value eventually, no value has been found in queryable");
  }
}
