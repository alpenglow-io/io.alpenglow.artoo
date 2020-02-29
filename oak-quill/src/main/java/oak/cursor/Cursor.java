package oak.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;
import static java.util.Arrays.copyOf;
import static oak.type.Nullability.nullable;

@SuppressWarnings("unchecked")
public interface Cursor<E> extends Iterator<E> {
  @SafeVarargs
  @NotNull
  @Contract("_ -> new")
  static <T> Cursor<T> many(final T... values) {
    return new Many<>(Arrays.copyOf(nonNullable(values, "values"), values.length));
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
  static Cursor<Double> many(final double[] values) {
    return new CursorDoubles(copyOf(values, values.length));
  }

  static <T> Cursor<T> of(final T value) {
    return nullable(value, One::new, () -> (Cursor<T>) Default.None);
  }

  @NotNull
  static <T> Cursor<T> none() {
    return Cursor.of(null);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2> oak.cursor.$2.Cursor<V1, V2> of(final V1 value1, final V2 value2) {
    return nullable(value1, value2, oak.cursor.$2.Cursor::one, () -> ) ;
  }
}


