package dev.lug.oak.quill.tuple;

import dev.lug.oak.func.pre.Predicate2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.nonNull;

public interface Tuple {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Tuple2<V1, V2> of(V1 v1, V2 v2) {
    return new Pair<>(v1, v2);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <V1, V2> Tuple2<V1, V2> none2() {
    return new None2<>();
  }

  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
    return new Triple<>(v1, v2, v3);
  }

  @NotNull
  static  <T1, T2> Predicate2<T1, T2> areNonNull() {
    return (t1, t2) -> nonNull(t1) && nonNull(t2);
  }
}
