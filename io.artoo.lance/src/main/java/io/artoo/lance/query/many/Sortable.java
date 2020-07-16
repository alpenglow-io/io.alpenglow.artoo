package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.cursor.Cursor;

import java.util.Arrays;

import static io.artoo.lance.query.cursor.Cursor.*;
import static io.artoo.lance.query.operation.Where.on;

public interface Sortable<T> extends Queryable<T> {
  default Many<T> order() {
    return () -> order(cursor());
  }

  @SuppressWarnings("unchecked")
  private Cursor<T> order(Cursor<T> cursor) throws Throwable {
    if (!cursor.hasNext()) return cursor;

    var ordering = (T[]) new Object[1];
    ordering[0] = cursor.fetch();

    var index = 0;
    while (cursor.hasNext()) {
      var element = cursor.fetch();
      ordering = Arrays.copyOf(ordering, ordering.length + 1);

      var position = 0;
      while (position < ordering.length - 1 && element.hashCode() > ordering[position].hashCode()) {
        position++;
      }
      for (var i = ordering.length - 1; i > position; i--) {
        ordering[i] = ordering[i - 1];
      }

      ordering[position] = element;
    }

    return Cursor.every(ordering);
  }
}
