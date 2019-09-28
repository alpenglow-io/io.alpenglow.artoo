package oak.quill.query.tuple;

import oak.collect.Many;
import oak.func.fun.Function2;
import oak.quill.Structable;
import oak.quill.query.Queryable;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;
import static oak.type.Nullability.nonNullableState;

public interface Queryables2<V1, V2> extends Structable<Tuple2<V1, V2>> {
  static <T1, T2> Queryables2<T1, T2> of(final Many<Tuple2<T1, T2>> tuples) {
    return new QueryTuple2<>(nonNullable(tuples, "tuples"));
  }

  <R> Queryable<R> select(Function2<? super V1, ? super V2, ? extends  R> map);
  <R, S extends Structable<R>> Queryable<R> selection(Function2<? super V1, ? super V2, ? extends S> flatMap);
}

final class QueryTuple2<V1, V2> implements Queryables2<V1, V2> {
  private final Many<Tuple2<V1, V2>> tuples;

  @Contract(pure = true)
  QueryTuple2(final Many<Tuple2<V1, V2>> tuples) {this.tuples = tuples;}

  @NotNull
  @Override
  public final Iterator<Tuple2<V1, V2>> iterator() {
    return tuples.iterator();
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final <R> Queryable<R> select(final Function2<? super V1, ? super V2, ? extends R> map) {
    return () -> {
      final var result = Many.<R>of();
      for (final var tuple : tuples) {
        result.add(
          tuple
            .select(nonNullableState(map, "Queryables2"))
            .or("Map went wrong", IllegalStateException::new)
            .get()
        );
      }
      return result.iterator();
    };
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final <R, S extends Structable<R>> Queryable<R> selection(final Function2<? super V1, ? super V2, ? extends S> flatMap) {
    return () -> {
      final var result = Many.<R>of();
      for (final var tuple : tuples) {
        for (final var value : tuple
          .select(nonNullableState(flatMap, "Queryables2"))
          .or("FlatMap went wrong", IllegalArgumentException::new)
          .get()
        ) result.add(value);
      }
      return result.iterator();
    };
  }
}
