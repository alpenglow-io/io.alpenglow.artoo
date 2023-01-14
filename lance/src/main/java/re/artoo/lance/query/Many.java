package re.artoo.lance.query;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.many.*;
import re.artoo.lance.query.many.pairs.Pairable;

@FunctionalInterface
public interface Many<T> extends
  Aggregatable<T>,
  Concatenatable<T>,
  Convertable<T>,
  Filterable<T>,
  Joinable<T>,
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
    return () -> Cursor.open(items);
  }

  static <R> Many<R> from(final TrySupplier1<R[]> supply) {
    return () -> Cursor.open(supply.get());
  }

  static Many<Object> fromAny(Object... objects) {
    return () -> Cursor.open(objects);
  }

  static <R> Many<R> empty() {
    return Cursor::empty;
  }

  static Many<Integer> ints(final int start, final int end) {
    return new Ints(start, end);
  }

  static <R> Many<R> of(final Cursor<R> cursor) {
    return () -> cursor;
  }

  static <R> Many<R> from(Iterable<R> iterable) {
    return () -> Cursor.iteration(iterable.iterator());
  }

  interface Pairs<A, B> extends Pairable<A, B> {}
}

final class Ints implements Many<Integer> {
  private final int start;
  private final int end;

  Ints(final int start, final int end) {
    assert start < end;
    this.start = start;
    this.end = end;
  }

  @Override
  public Cursor<Integer> cursor() {
    final var numbers = new Integer[end - start + 1];
    for (int number = start, index = 0; number <= end; number++, index++) {
      numbers[index] = number;
    }
    return Cursor.open(numbers);
  }
}

final class Supplied<R> implements Many<R> {
  private final TrySupplier1<R[]> supply;

  Supplied(final TrySupplier1<R[]> supply) {this.supply = supply;}

  @Override
  public Cursor<R> cursor() {
    try {
      return Cursor.open(supply.invoke());
    } catch (Throwable cause) {
      cause.printStackTrace();
      return Cursor.empty();
    }
  }
}
