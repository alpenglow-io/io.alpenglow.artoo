package re.artoo.lance;

import re.artoo.lance.query.Cursor;

import java.util.Iterator;

public interface Queryable<T> extends Iterable<T> {
  Cursor<T> cursor() throws Throwable;

  @Override
  default Iterator<T> iterator() {
    try {
      return cursor();
    } catch (Throwable throwable) {
      throw new Cursor.Exception("Can't run origin, since %s".formatted(throwable.getMessage()), throwable);
    }
  }
}
