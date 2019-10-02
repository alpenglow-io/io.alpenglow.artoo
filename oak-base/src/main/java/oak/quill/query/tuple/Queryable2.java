package oak.quill.query.tuple;

import oak.collect.Many;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Queryable2<V1, V2> extends Projectable2<V1, V2>, Filterable2<V1, V2> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T1, T2> Queryable2<T1, T2> of(final Tuple2<T1, T2>... tuples) {
    return new QueryTuple2<>(Many.of(nonNullable(tuples, "tuples")));
  }

  @NotNull
  @Contract("_ -> new")
  static <T1, T2> Queryable2<T1, T2> of(final Many<Tuple2<T1, T2>> tuples) {
    return new QueryTuple2<>(nonNullable(tuples, "tuples"));
  }
}

final class QueryTuple2<V1, V2> implements Queryable2<V1, V2> {
  private final Many<Tuple2<V1, V2>> tuples;

  @Contract(pure = true)
  QueryTuple2(final Many<Tuple2<V1, V2>> tuples) {this.tuples = tuples;}

  @NotNull
  @Override
  public final Iterator<Tuple2<V1, V2>> iterator() {
    return tuples.iterator();
  }
}
