package dev.lug.oak.quill.query.tuple;

import dev.lug.oak.collect.Many;
import dev.lug.oak.quill.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Queryable3<V1, V2, V3> extends Projectable3<V1, V2, V3>, Filterable3<V1, V2, V3> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T1, T2, T3> Queryable3<T1, T2, T3> of(final Tuple3<T1, T2, T3>... tuples) {
    return new QueryTuple3<>(Many.of(nonNullable(tuples, "tuples")));
  }

  @NotNull
  @Contract("_ -> new")
  static <T1, T2, T3> Queryable3<T1, T2, T3> of(final Many<Tuple3<T1, T2, T3>> tuples) {
    return new QueryTuple3<>(nonNullable(tuples, "tuples"));
  }
}

final class QueryTuple3<V1, V2, V3> implements Queryable3<V1, V2, V3> {
  private final Many<Tuple3<V1, V2, V3>> tuples;

  @Contract(pure = true)
  QueryTuple3(final Many<Tuple3<V1, V2, V3>> tuples) {this.tuples = tuples;}

  @NotNull
  @Override
  public final Iterator<Tuple3<V1, V2, V3>> iterator() {
    return tuples.iterator();
  }
}