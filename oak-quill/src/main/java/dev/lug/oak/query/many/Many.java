package dev.lug.oak.query.many;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;

public interface Many<T> extends
  Projectable<T>, Filterable<T>, Partitionable<T>, Uniquable<T>, Aggregatable<T>, Concatenatable<T>, Groupable<T>,
  Joinable<T>, Quantifiable<T>, Settable<T>, Insertable<T> {

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T> Many<T> from(final T... items) {
    return new Array<>(Arrays.asList(Arrays.copyOf(items, items.length)));
  }

  @NotNull
  @Contract("_ -> new")
  static <T> Many<T> from(final Iterable<T> iterable) {
    return new Array<>(nonNullable(iterable, "iterable"));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <T> Many<T> empty() {
    return (Many<T>) DefaultMany.Empty;
  }

  @NotNull
  @Contract("_, _ -> new")
  static <S> Many<S> repeat(final Supplier1<? extends S> supplier, final int count) {
    return new Repeat<>(nonNullable(supplier, "supplier"), count);
  }
}

enum DefaultMany {
  ;

  static final Many<?> Empty = new Empty<>();
}

final class Empty<Q> implements Many<Q> {
  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<Q> iterator() {
    return Cursor.none();
  }
}

final class Repeat<T> implements Many<T> {
  private final Supplier1<? extends T> supplier;
  private final int count;

  @Contract(pure = true)
  Repeat(final Supplier1<? extends T> supplier, final int count) {
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

final class Array<T> implements Many<T> {
  private final Iterable<T> iterable;

  @Contract(pure = true)
  Array(final Iterable<T> iterable) {
    this.iterable = iterable;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return iterable.iterator();
  }
}
