package dev.lug.oak.query.many2;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.type.union.Union2;
import dev.lug.oak.query.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dev.lug.oak.query.Queryable.P.as;
import static dev.lug.oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Many2<V1, V2> extends Projectable2<V1, V2>, Filterable2<V1, V2> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T1, T2> Many2<T1, T2> of(final Tuple2<T1, T2>... tuples) {
    nonNullable(tuples, "tuples");
    return of(() -> Cursor.of(tuples));
  }

  @NotNull
  @Contract("_ -> new")
  static <T1, T2> Many2<T1, T2> of(final Iterable<Tuple2<T1, T2>> tuples) {
    nonNullable(tuples, "tuples");
    return () -> {
      final var array = new ArrayList<Union2<T1, T2>>();
      for (final var tuple : tuples)
        tuple
          .select(as(Union2::of))
          .eventually(array::add);
      return array.iterator();
    };
  }
}
