package io.artoo.lance.query;

import io.artoo.lance.next.Cursor;
import io.artoo.lance.query.many.Aggregatable;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.query.many.Filterable;
import io.artoo.lance.query.many.Otherwise;
import io.artoo.lance.query.many.Partitionable;
import io.artoo.lance.query.many.Peekable;
import io.artoo.lance.query.many.Projectable;
import io.artoo.lance.query.many.Quantifiable;
import io.artoo.lance.query.many.Settable;
import io.artoo.lance.query.many.Sortable;
import io.artoo.lance.query.many.Uniquable;

public interface Many<T> extends
  Projectable<T>,
  Filterable<T>,
  Partitionable<T>,
  Uniquable<T>,
  Aggregatable<T>,
  Quantifiable<T>,
  Settable<T>,
  Concatenatable<T>,
  Otherwise<T>,
  Peekable<T>,
  Sortable<T>
{

  @SafeVarargs
  static <R> Many<R> from(final R... items) {
    return new Wany<>(Cursor.every(items));
  }

  static Many<Object> fromAny(Object... objects) {
    return new Wany<>(Cursor.every(objects));
  }

  static <R> Many<R> empty() {
    return new Empty<>(Cursor.nothing());
  }

  static Many<Integer> ints(final int start, final int end) {
    return new Ints(start, end);
  }

  static <R> Many<R> wany(final Cursor<R> cursor) {
    return new Wany<>(cursor);
  }
}

record Empty<T>(Cursor<T> cursor) implements Many<T> {}

record Wany<T>(Cursor<T> cursor) implements Many<T> {}

final class Ints implements Many<Integer> {
  private final int start;
  private final int end;

  Ints(final int start, final int end) {
    assert start < end;
    this.start = start;
    this.end = end;
  }


  @Override
  public final Cursor<Integer> cursor() {
    final var numbers = new Integer[end - start + 1];
    for (int number = start, index = 0; number <= end; number++, index++) {
      numbers[index] = number;
    }
    return Cursor.every(numbers);
  }
}
