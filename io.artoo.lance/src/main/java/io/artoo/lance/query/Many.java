package io.artoo.lance.query;

import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.many.Aggregatable;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.query.many.Convertable;
import io.artoo.lance.query.many.Filterable;
import io.artoo.lance.query.many.Joinable;
import io.artoo.lance.query.many.Matchable;
import io.artoo.lance.query.many.Otherwise;
import io.artoo.lance.query.many.Partitionable;
import io.artoo.lance.query.many.Peekable;
import io.artoo.lance.query.many.Projectable;
import io.artoo.lance.query.many.Quantifiable;
import io.artoo.lance.query.many.Settable;
import io.artoo.lance.query.many.Sortable;
import io.artoo.lance.query.many.Uniquable;
import io.artoo.lance.query.many.oftwo.Pairable;
import io.artoo.lance.tuple.Pair;

public interface Many<T> extends
  Aggregatable<T>,
  Concatenatable<T>,
  Convertable<T>,
  Filterable<T>,
  Joinable<T>,
  Matchable<T>,
  Otherwise<T>,
  Partitionable<T>,
  Peekable<T>,
  Projectable<T>,
  Quantifiable<T>,
  Settable<T>,
  Sortable<T>,
  Uniquable<T> {

  @SafeVarargs
  static <R> Many<R> from(final R... items) {
    return new Some<>(Cursor.open(items));
  }

  static Many<Object> fromAny(Object... objects) {
    return new Some<>(Cursor.open(objects));
  }

  static <R> Many<R> empty() {
    return new Empty<>();
  }

  static Many<Integer> ints(final int start, final int end) {
    return new Ints(start, end);
  }

  static <R> Many<R> of(final Cursor<R> cursor) {
    return new Some<>(cursor);
  }

  static <R> Many<R> from(Iterable<R> iterable) {
    return Many.of(Cursor.iteration(iterable.iterator()));
  }

  interface OfTwo<A, B> extends Pairable<A, B> {}
}

final class Empty<T> implements Many<T> {
  @Override
  public final Cursor<T> cursor() {
    return Cursor.nothing();
  }
}

record Some<T>(Cursor<T> cursor) implements Many<T> {}

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
    return Cursor.open(numbers);
  }
}
