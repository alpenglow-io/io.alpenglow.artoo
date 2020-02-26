package oak.cursor;

import oak.func.Func;
import oak.func.Suppl;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;
import static java.util.Arrays.copyOf;

@SuppressWarnings("unchecked")
public interface Cursor<E> extends Iterator<E> {
  @SafeVarargs
  @NotNull
  @Contract("_ -> new")
  static <T> Cursor<T> all(final T... values) {
    return new All<>(Arrays.copyOf(Nullability.nonNullable(values, "values"), values.length));
  }

  @NotNull
  @Contract(" -> new")
  static Index index() {
    return new Index(0);
  }

  @NotNull
  @Contract("_ -> new")
  static Index index(final int start) {
    return new Index(start);
  }

  @NotNull
  @Contract("_ -> new")
  static Cursor<Double> all(final double[] values) {
    return new CursorDoubles(copyOf(values, values.length));
  }

  @NotNull
  @Contract("_ -> new")
  static <T> Cursor<T> once(T value) {
    return new Once<>(Nullability.nonNullable(value, "value"));
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2> oak.cursor.$2.Cursor<V1, V2> once(final V1 value1, final V2 value2) {
    return oak.cursor.$2.Cursor.once(value1, value2);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <T> Cursor<T> none() {
    return (Cursor<T>) Default.None;
  }

  enum Default implements Cursor<Object> {
    None;

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Object next() {
      return null;
    }
  }

  @NotNull
  @Contract("_, _ -> new")
  static <T> Cursor<T> jump(final Iterator<T> first, final Iterator<T> next) {
    return new Jump<>(Nullability.nonNullable(first, "first"), Nullability.nonNullable(next, "next"));
  }

  static <R> Cursor<R> ofNullable(final R value) {
    return Nullability.nullable(value, Cursor::once, Cursor::none);
  }

  static <T, R> Cursor<R> ofSingle(final Iterable<T> iterable, Func<? super T, ? extends R> then) {
    return ofSingle(iterable, then, Suppl.none());
  }

  static <T, R> Cursor<R> ofSingle(final Iterable<T> iterable, Func<? super T, ? extends R> then, Suppl<? extends R> otherwise) {
    final var iter = Nullability.nonNullable(iterable, "iterable").iterator();
    return Cursor.ofNullable(iter.hasNext() ? Nullability.nonNullable(then, "then").apply(iter.next()) : Nullability.nonNullable(otherwise, "otherwise").get());
  }
}

