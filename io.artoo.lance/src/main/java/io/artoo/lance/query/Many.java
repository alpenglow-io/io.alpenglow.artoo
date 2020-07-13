package io.artoo.lance.query;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.query.many.Aggregatable;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.query.many.Filterable;
import io.artoo.lance.query.many.Otherwise;
import io.artoo.lance.query.many.Partitionable;
import io.artoo.lance.query.many.Peekable;
import io.artoo.lance.query.many.Projectable;
import io.artoo.lance.query.many.Quantifiable;
import io.artoo.lance.query.many.Settable;
import io.artoo.lance.query.many.Uniquable;

import java.util.Arrays;

public interface  Many<T> extends
  Projectable<T>,
  Filterable<T>,
  Partitionable<T>,
  Uniquable<T>,
  Aggregatable<T>,
  Quantifiable<T>,
  Settable<T>,
  Concatenatable<T>,
  Otherwise<T>,
  Peekable<T>
{

  @SafeVarargs
  static <R> Many<R> from(final R... items) {
    return () -> Cursor.every(Arrays.copyOf(items, items.length));
  }

  static Many<Object> fromAny(Object... objects) {
    return new Array<>(objects);
  }

  static <R> Many<R> empty() {
    return new Empty<>();
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
    return Cursor.every(Arrays.copyOf(elements, elements.length));
  }
}

final class Empty<T> implements Many<T> {
  @Override
  public final Cursor<T> cursor() {
    return Cursor.every();
  }
}

