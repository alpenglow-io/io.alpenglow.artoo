package oak.quill.tuple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Tuple {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Tuple2<V1, V2> of(V1 v1, V2 v2) {
    return new Pair<>(v1, v2);
  }

  static <V1, V2> Tuple2<V1, V2> none2() {
    return new None2<>();
  }

  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
    return new Triple<>(v1, v2, v3);
  }
}
