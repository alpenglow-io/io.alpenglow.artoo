package io.artoo.lance.query;

import io.artoo.lance.query.cursor.Cursor;

import io.artoo.lance.query.many.Aggregatable;
import io.artoo.lance.query.many.Filterable;
import io.artoo.lance.query.many.Insertable;
import io.artoo.lance.query.many.Otherwise;
import io.artoo.lance.query.many.Partitionable;
import io.artoo.lance.query.many.Projectable;
import io.artoo.lance.query.many.Quantifiable;
import io.artoo.lance.query.many.Settable;
import io.artoo.lance.query.many.Uniquable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface  Many<T> extends
  Projectable<T>,
  Filterable<T>,
  Partitionable<T>,
  Uniquable<T>,
  Aggregatable<T>,
  Quantifiable<T>,
  Settable<T>,
  Insertable<T>,
  Otherwise<T>
{

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <R> Many<R> from(final R... items) {
    return new Array<>(items);
  }

  @Contract("_ -> new")
  static @NotNull Many<Object> fromAny(Object... objects) {
    return new Array<>(Arrays.copyOf(objects, objects.length));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <R> Many<R> none() {
    return (Many<R>) ManyRecord.None;
  }

  @NotNull
  @Contract("_, _ -> new")
  static <R> Many<R> repeat(final Supplier<? extends R> supplier, final int count) {
    return new Repeat<>(nonNullable(supplier, "supplier"), count);
  }
}

final class Array<T> implements Many<T> {
  private final T[] elements;

  @SafeVarargs
  @Contract(pure = true)
  Array(final T... elements) {
    this.elements = elements;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    return Cursor.pipe(Arrays.copyOf(elements, elements.length));
  }
}

final class Repeat<T> implements Many<T> {
  private final Supplier<? extends T> supplier;
  private final int count;

  @Contract(pure = true)
  public Repeat(final Supplier<? extends T> supplier, final int count) {
    this.supplier = supplier;
    this.count = count;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    var cursor = Cursor.<T>empty();
    for (var index = 0; index < count; index++) {
      cursor = cursor.append(supplier.get());
    }
    return cursor;
  }
}

enum ManyRecord implements Many<Record> {
  None;

  @NotNull
  @Override
  public final Cursor<Record> cursor() {
    return Cursor.empty();
  }
}

