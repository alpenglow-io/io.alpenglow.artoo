package oak.type.tuple;

import oak.func.fun.Function1;

import static java.util.Objects.requireNonNull;

public interface Tuple {
  static <V> Tuple1<V> of(V value) {
    return new Tuple1<>(value);
  }

  static <V1, V2> Tuple2<V1, V2> of(V1 v1, V2 v2) {
    return new Tuple2<>(v1, v2);
  }

  static <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
    return new Tuple3<>(v1, v2, v3);
  }

  @SuppressWarnings("unchecked")
  default <T extends Tuple, R> R as(Function1<T, R> f) {
    return requireNonNull(f, "As must be not null").apply((T) this);
  }
}
