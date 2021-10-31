package io.artoo.lance.query;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.many.Aggregatable;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.query.many.Convertable;
import io.artoo.lance.query.many.Filterable;
import io.artoo.lance.query.many.Joinable;
import io.artoo.lance.query.many.Matchable;
import io.artoo.lance.query.many.Elseable;
import io.artoo.lance.query.many.Partitionable;
import io.artoo.lance.query.many.Peekable;
import io.artoo.lance.query.many.Projectable;
import io.artoo.lance.query.many.Quantifiable;
import io.artoo.lance.query.many.Settable;
import io.artoo.lance.query.many.Sortable;
import io.artoo.lance.query.many.Uniquable;
import io.artoo.lance.query.many.oftwo.Pairable;

public interface Many<T> extends
  Aggregatable<T>,
  Concatenatable<T>,
  Convertable<T>,
  Filterable<T>,
  Joinable<T>,
  Matchable<T>,
  Elseable<T>,
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

  static <R> Many<R> from(final Suppl.MaybeSupplier<R[]> supply) {
    return new Supplied<>(supply);
  }

  static Many<Object> fromAny(Object... objects) {
    return new Some<>(Cursor.open(objects));
  }

  static <R> Many<R> empty() {
    return Cursor::nothing;
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

final class Supplied<R> implements Many<R> {
  private final Suppl.MaybeSupplier<R[]> supply;

  Supplied(final Suppl.MaybeSupplier<R[]> supply) {this.supply = supply;}

  @Override
  public Cursor<R> cursor() {
    try {
      return Cursor.open(supply.tryGet());
    } catch (Throwable cause) {
      cause.printStackTrace();
      return Cursor.nothing();
    }
  }
}
