package dev.lug.oak.query.many2;

import dev.lug.oak.query.Tuple2;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

@FunctionalInterface
public interface Many2<V1, V2> extends Projectable2<V1, V2>, Filterable2<V1, V2> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T1, T2> Many2<T1, T2> of(final Tuple2<T1, T2>... tuples) {
    return new Array2<>(List.of(Nullability.nonNullable(tuples, "tuples")));
  }

  @NotNull
  @Contract("_ -> new")
  static <T1, T2> Many2<T1, T2> of(final Iterable<Tuple2<T1, T2>> tuples) {
    return new Array2<>(Nullability.nonNullable(tuples, "tuples"));
  }
}

final class Array2<V1, V2> implements Many2<V1, V2> {
  private final Iterable<Tuple2<V1, V2>> tuples;

  @Contract(pure = true)
  Array2(final Iterable<Tuple2<V1, V2>> tuples) {
    this.tuples = tuples;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<V1, V2>> iterator() {
    return tuples.iterator();
  }
}
