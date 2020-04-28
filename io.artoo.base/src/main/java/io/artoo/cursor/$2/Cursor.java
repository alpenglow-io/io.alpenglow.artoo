package io.artoo.cursor.$2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.collect.$2.Iterator;
import io.artoo.union.$2.Union;

import java.util.Arrays;

import static io.artoo.type.Nullability.nonNullable;

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
    return new Many<>(Arrays.copyOf(unions, unions.length), io.artoo.cursor.Cursor.index());
  }

  @NotNull
  @Contract("_ -> new")
  static <V1, V2> Cursor<V1, V2> from(final java.util.Iterator<Union<V1, V2>> iterator) {
    return new Wrapped<>(nonNullable(iterator, "iterator"));
  }

  @SuppressWarnings("unchecked")
  static <V1, V2> Cursor<V1, V2> none() {
    return (Cursor<V1, V2>) Default.None;
  }

  @Override
  default Union<V1, V2> next() {
    return this;
  }
}
