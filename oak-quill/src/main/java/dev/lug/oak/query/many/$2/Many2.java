package dev.lug.oak.query.many.$2;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.union.$2.Union;
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
    return of(() -> Cursor.all(tuples));
  }

  @NotNull
  @Contract("_ -> new")
  static <T1, T2> Many2<T1, T2> of(final Iterable<Tuple2<T1, T2>> tuples) {
    nonNullable(tuples, "tuples");
    return () -> {
      final var array = new ArrayList<Union<T1, T2>>();
      for (final var tuple : tuples)
        tuple
          .select(as(Union::of))
          .eventually(array::add);
      return array.iterator();
    };
  }
}
