package io.artoo.query;

import io.artoo.cursor.Cursor;
import io.artoo.query.many.Aggregatable;
import io.artoo.query.many.Concatenatable;
import io.artoo.query.many.Otherwise;
import io.artoo.query.many.Filterable;
import io.artoo.query.many.Groupable;
import io.artoo.query.many.Insertable;
import io.artoo.query.many.Joinable;
import io.artoo.query.many.Partitionable;
import io.artoo.query.many.Projectable;
import io.artoo.query.many.Quantifiable;
import io.artoo.query.many.Settable;
import io.artoo.query.many.Uniquable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Supplier;

import static io.artoo.type.Nullability.nonNullable;

public interface Many<R extends Record> extends
  Projectable<R>, Filterable<R>, Partitionable<R>, Uniquable<R>, Aggregatable<R>, Concatenatable<R>, Groupable<R>,
  Joinable<R>, Quantifiable<R>, Settable<R>, Insertable<R>, Otherwise<R> {

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <R extends Record> Many<R> from(final R... items) {
    return new Records<>(Arrays.copyOf(items, items.length));
  }

  @NotNull
  @Contract("_ -> new")
  static <R extends Record> Many<R> from(final Iterable<R> iterable) {
    return new Iteration<>(nonNullable(iterable, "iterable"));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <R extends Record> Many<R> none() {
    return (Many<R>) ManyRecord.None;
  }

  @NotNull
  @Contract("_, _ -> new")
  static <R extends Record> Many<R> repeat(final Supplier<? extends R> supplier, final int count) {
    return new Repeat<>(nonNullable(supplier, "supplier"), count);
  }
}

final class Iteration<T extends Record> implements Many<T> {
  private final Iterable<T> iterable;

  @Contract(pure = true)
  Iteration(final Iterable<T> iterable) {this.iterable = iterable;}

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return iterable.iterator();
  }
}

final class Records<T extends Record> implements Many<T> {
  private final T[] records;

  @SafeVarargs
  @Contract(pure = true)
  Records(final T... records) {
    this.records = records;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.many(records);
  }
}

final class Repeat<T extends Record> implements Many<T> {
  private final Supplier<? extends T> supplier;
  private final int count;

  @Contract(pure = true)
  public Repeat(final Supplier<? extends T> supplier, final int count) {
    this.supplier = supplier;
    this.count = count;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    for (var index = 0; index < count; index++) {
      array.add(supplier.get());
    }
    return array.iterator();
  }
}

enum ManyRecord implements Many<Record> {
  None;

  @NotNull
  @Override
  public final Iterator<Record> iterator() {
    return Cursor.none();
  }
}

