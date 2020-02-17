package dev.lug.oak.query;

import dev.lug.oak.cursor.$3.Cursor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Tuple {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Tuple2<V1, V2> of(V1 v1, V2 v2) {
    return () -> dev.lug.oak.cursor.$2.Cursor.once(v1, v2);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <V1, V2> Tuple2<V1, V2> none2() {
    return dev.lug.oak.cursor.$2.Cursor::none;
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3> Tuple3<V1, V2, V3> none3() {
    return Cursor::none;
  }

  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
    return () -> Cursor.once(v1, v2, v3);
  }
}

