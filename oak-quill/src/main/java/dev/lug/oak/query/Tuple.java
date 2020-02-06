package dev.lug.oak.query;

import dev.lug.oak.query.tuple3.Default3;
import dev.lug.oak.query.tuple3.None3;
import dev.lug.oak.query.tuple3.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface Tuple {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Tuple2<V1, V2> of(V1 v1, V2 v2) {
    return new Pair<>(v1, v2);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <V1, V2> Tuple2<V1, V2> none2() {
    return (Tuple2<V1, V2>) DefaultTuple.None2;
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3> Tuple3<V1, V2, V3> none3() {
    return (Tuple3<V1, V2, V3>) DefaultTuple.None3;
  }

  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
    return new Default3<>(v1, v2, v3);
  }
}

enum DefaultTuple {
  ;
  public static Tuple2<?, ?> None2 = new None2<>();
  public static Tuple3<?, ?, ?> None3 = new None3<>();
}
