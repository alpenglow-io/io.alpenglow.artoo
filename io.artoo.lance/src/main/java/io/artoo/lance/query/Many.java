package io.artoo.lance.query;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.cursor.Cursor;
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
    return new Array<>(items);
  }

  static Many<Object> fromAny(Object... objects) {
    return new Array<>(objects);
  }

  static <R> Many<R> empty() {
    return new Empty<>(Cursor.nothing());
  }

  static <R> Many<R> from(final Iterable<R> iterable) {
    return new Iteration<>(iterable);
  }

  static Many<Integer> ints(final int start, final int end) {
    return new Ints(start, end);
  }

  static <R> Many<R> resultSet(final Suppl.Uni<Cursor<R>> cursor) {
    return new ResultSet<>(cursor);
  }
}

final class Array<T> implements Many<T> {
  private final T[] elements;

  @SafeVarargs
  Array(final T... elements) {
    this.elements = elements;
  }

  @Override
  public final Cursor<T> cursor() {
    return Cursor.every(elements);
  }
}

record Empty<T>(Cursor<T> cursor) implements Many<T> {}

final class Iteration<T> implements Many<T> {
  private final Iterable<T> iterable;

  Iteration(final Iterable<T> iterable) {this.iterable = iterable;}

  @Override
  public final Cursor<T> cursor() {
    return Cursor.iteration(iterable.iterator());
  }
}

final class ResultSet<R> implements Many<R> {
  private final Suppl.Uni<Cursor<R>> cursor;

  ResultSet(final Suppl.Uni<Cursor<R>> cursor) {this.cursor = cursor;}

  @Override
  public final Cursor<R> cursor() throws Throwable {
    return cursor.tryGet();
  }
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
  public final Cursor<Integer> cursor() {
    final var numbers = new Integer[end - start + 1];
    for (int number = start, index = 0; number <= end; number++, index++) {
      numbers[index] = number;
    }
    return Cursor.every(numbers);
  }
}
