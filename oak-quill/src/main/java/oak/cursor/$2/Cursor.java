package oak.cursor.$2;

import oak.collect.$2.Iterator;
import oak.func.$2.Func;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static oak.type.Nullability.nonNullable;

public interface Cursor<V1, V2> extends Iterator<V1, V2>, Union<V1, V2> {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <T1, T2> Cursor<T1, T2> one(T1 value1, T2 value2) {
    return new One<>(value1, value2);
  }

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <V1, V2> Cursor<V1, V2> many(final Union<V1, V2>... unions) {
    return new Many<>(Arrays.copyOf(unions, unions.length), oak.cursor.Cursor.index());
  }

  @NotNull
  @Contract("_ -> new")
  static <V1, V2> Cursor<V1, V2> from(final java.util.Iterator<Union<V1, V2>> iterator) {
    return new Wrapped<>(nonNullable(iterator, "iterator"));
  }

  @Override
  default Union<V1, V2> next() { return this; }
}
