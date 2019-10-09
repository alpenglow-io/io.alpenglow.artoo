package oak.collect.cursor;

import oak.func.fun.Function1;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.copyOf;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static oak.type.Any.nonNullOrElse;
import static oak.type.Nullability.nonNullable;

public interface Cursor<E> extends Iterator<E>, Enumeration<E> {
  @NotNull
  @Contract("_ -> new")
  static <T> Cursor<T> of(final T[] values) {
    return new Forward<>(copyOf(nonNullable(values, "values"), values.length));
  }

  @NotNull
  @Contract("_ -> new")
  static Cursor<Double> of(final double[] values) {
    return new CursorDoubles(copyOf(values, values.length));
  }

  @NotNull
  @Contract("_ -> new")
  static <T> Cursor<T> of(T value) {
    return new Once<>(nonNullable(value, "value"));
  }

  @NotNull
  @Contract("_ -> new")
  static <T> Cursor<T> ofAny(T value) {
    return new Once<>(nonNullable(value, "value"));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <T> Cursor<T> none() {
    return new None<>();
  }

  @NotNull
  @Contract("_, _ -> new")
  static <T> Cursor<T> jump(final Iterator<T> first, final Iterator<T> next) {
    return new Jump<>(nonNullable(first, "first"), nonNullable(next, "next"));
  }

  static <R> Cursor<R> ofNullable(final R value) {
    return nonNullOrElse(value, Cursor::of, Cursor::none);
  }

  static <T, R> Cursor<R> ofSingle(final Iterable<T> iterable, Function1<? super T, ? extends R> then) {
    return ofSingle(iterable, then, Supplier1.none());
  }

  static <T, R> Cursor<R> ofSingle(final Iterable<T> iterable, Function1<? super T, ? extends R> then, Supplier1<? extends R> otherwise) {
    final var iter = nonNullable(iterable, "iterable").iterator();
    return Cursor.ofNullable(iter.hasNext() ? nonNullable(then, "then").apply(iter.next()) : nonNullable(otherwise, "otherwise").get());
  }

  @Override
  default boolean hasMoreElements() {
    return hasNext();
  }

  @Override
  default E nextElement() {
    return next();
  }

  @Override
  default Iterator<E> asIterator() {
    return this;
  }
}

final class CursorDoubles implements Cursor<Double> {
  private final double[] doubles;
  private final AtomicInteger index;

  CursorDoubles(final double[] doubles) {
    this(
      doubles,
      new AtomicInteger(0)
    );
  }
  @Contract(pure = true)
  private CursorDoubles(double[] doubles, final AtomicInteger index) {
    this.doubles = doubles;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return doubles.length > 0 && index.get() < doubles.length;
  }

  @Override
  @Nullable
  public final Double next() {
    return doubles.length > 0 ? doubles[index.getAndIncrement()] : null;
  }
}
