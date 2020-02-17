package dev.lug.oak.cursor.$2;

import dev.lug.oak.collect.$2.Iterator;
import dev.lug.oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public interface Cursor<V1, V2> extends Iterator<V1, V2>, Union<V1, V2> {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <T1, T2> Cursor<T1, T2> once(T1 value1, T2 value2) {
    return new Once<>(value1, value2);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2> Cursor<V1, V2> none() {
    return new None<>();
  }

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <V1, V2> Cursor<V1, V2> all(final Union<V1, V2>... unions) {
    return new All<>(Arrays.copyOf(unions, unions.length), dev.lug.oak.cursor.Cursor.index());
  }

  @Override
  default Union<V1, V2> next() { return this; }
}
