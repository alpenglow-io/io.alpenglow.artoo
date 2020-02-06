package dev.lug.oak.query.tuple2;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.collect.cursor.Cursor2;
import dev.lug.oak.query.One;
import dev.lug.oak.query.Queryable.Tuple2AsAny;
import dev.lug.oak.query.Queryable.Tuple2AsTuple;
import dev.lug.oak.query.Queryable2;
import dev.lug.oak.query.Tuple2;

public interface Projectable2<V1, V2> extends Queryable2<V1, V2> {
  default <T> One<T> select(final Tuple2AsAny<? super V1, ? super V2, ? extends T> map) {
    return () -> {
      for (final var tuple : this) return Cursor.once(tuple.as(map::apply));
      return Cursor.none();
    };
  }

  default <T1, T2> Tuple2<T1, T2> select(final Tuple2AsTuple<? super V1, ? super V2, ? extends Tuple2<T1, T2>> flatMap) {
    return () -> {
      for (final var tuple : this) return tuple.as(flatMap::apply).iterator();
      return Cursor2.none();
    };
  }
}
