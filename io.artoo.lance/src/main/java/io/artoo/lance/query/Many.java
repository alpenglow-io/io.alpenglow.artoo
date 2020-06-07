package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.many.Aggregatable;
import io.artoo.lance.query.many.Filterable;
import io.artoo.lance.query.many.Insertable;
import io.artoo.lance.query.many.Otherwise;
import io.artoo.lance.query.many.Partitionable;
import io.artoo.lance.query.many.Projectable;
import io.artoo.lance.query.many.Quantifiable;
import io.artoo.lance.query.many.Settable;
import io.artoo.lance.query.many.Uniquable;
import io.artoo.lance.value.Any;
import io.artoo.lance.value.Single32;
import io.artoo.lance.value.Single64;
import io.artoo.lance.value.Int16;
import io.artoo.lance.value.Int32;
import io.artoo.lance.value.Int64;
import io.artoo.lance.value.Int8;
import io.artoo.lance.value.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.artoo.lance.type.Nullability.nonNullable;
import static java.util.stream.Collectors.toList;

public interface Many<R extends Record> extends
  Projectable<R>, Filterable<R>, Partitionable<R>, Uniquable<R>, Aggregatable<R>,
  Quantifiable<R>, Settable<R>, Insertable<R>, Otherwise<R> {

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <R extends Record> Many<R> from(final R... items) {
    return new Records<>(Arrays.copyOf(items, items.length));
  }

  @Contract("_ -> new")
  static @NotNull Many<Any> fromAny(Object... objects) {
    return new Iteration<>(Stream.of(objects).filter(Objects::nonNull).map(Any::new).collect(toList()));
  }

  @Contract("_ -> new")
  static @NotNull Many<Text> from(String @NotNull ... strings) {
    List<Text> list = new ArrayList<>();
    for (final var value : strings) if (value != null) list.add(new Text(value));
    return new Iteration<>(list);
  }

  @Contract("_ -> new")
  static @NotNull Many<Int8> from(byte @NotNull ... bytes) {
    final var list = new ArrayList<Int8>();
    for (final var value : bytes) list.add(new Int8(value));
    return new Iteration<>(list);
  }

  @Contract("_ -> new")
  static @NotNull Many<Int16> from(short @NotNull ... shorts) {
    final var list = new ArrayList<Int16>();
    for (final var value : shorts) list.add(new Int16(value));
    return new Iteration<>(list);
  }

  @Contract("_ -> new")
  static @NotNull Many<Int32> from(int @NotNull ... ints) {
    final var list = new ArrayList<Int32>();
    for (final var value : ints) list.add(new Int32(value));
    return new Iteration<>(list);
  }

  @Contract("_ -> new")
  static @NotNull Many<Int64> from(long @NotNull ... longs) {
    final var list = new ArrayList<Int64>();
    for (final var value : longs) list.add(new Int64(value));
    return new Iteration<>(list);
  }

  @Contract("_ -> new")
  static @NotNull Many<Single32> from(float @NotNull ... floats) {
    final var list = new ArrayList<Single32>();
    for (final var value : floats) list.add(new Single32(value));
    return new Iteration<>(list);
  }

  @Contract("_ -> new")
  static @NotNull Many<Single64> from(double @NotNull ... doubles) {
    final var list = new ArrayList<Single64>();
    for (final var value : doubles) list.add(new Single64(value));
    return new Iteration<>(list);
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

  @Override
  public String toString() {
    return String.format("[%s]", iterable);
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

