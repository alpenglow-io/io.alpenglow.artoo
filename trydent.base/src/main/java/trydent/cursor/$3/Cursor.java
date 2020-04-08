package trydent.cursor.$3;

import trydent.collect.$3.Iterator;
import trydent.union.$3.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static trydent.cursor.Cursor.index;

@SuppressWarnings("unchecked")
public interface Cursor<V1, V2, V3> extends Iterator<V1, V2, V3>, Union<V1, V2, V3> {
  static <T1, T2, T3> Cursor<T1, T2, T3> all(final Union<T1, T2, T3>... unions) {
    return new All<>(Arrays.copyOf(unions, unions.length), index());
  }

  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <T1, T2, T3> Cursor<T1, T2, T3> once(final T1 value1, final T2 value2, final T3 value3) {
    return new Once<>(value1, value2, value3);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3> Cursor<V1, V2, V3> none() {
    return new None<>();
  }

  @Override
  default Union<V1, V2, V3> next() { return this; }

  @Override
  default boolean hasNext() { return false; }
}

