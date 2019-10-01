package oak.quill.query.tuple;

import oak.collect.Many;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Queryable2<V1, V2> extends Projectable2<V1, V2> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T1, T2, T extends Tuple2<T1, T2>> Queryable2<T1, T2> of(final T... tuples) {
    return new QueryTuple2<>(Many.of(nonNullable(tuples, "tuples")));
  }
}

final class QueryTuple2<V1, V2, T extends Tuple2<V1, V2>> implements Queryable2<V1, V2> {
  private final Many<T> tuples;

  @Contract(pure = true)
  QueryTuple2(final Many<T> tuples) {this.tuples = tuples;}

  @SuppressWarnings("unchecked")
  @NotNull
  @Override
  public final Iterator<Tuple2<V1, V2>> iterator() {
    return (Iterator<Tuple2<V1, V2>>) tuples.iterator();
  }
}
