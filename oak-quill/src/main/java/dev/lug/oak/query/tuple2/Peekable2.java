package dev.lug.oak.query.tuple2;

import dev.lug.oak.collect.cursor.Cursor2;
import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.query.Queryable2;
import dev.lug.oak.query.Tuple;
import dev.lug.oak.query.Tuple2;

import static dev.lug.oak.type.Nullability.nonNullable;

public interface Peekable2<V1, V2> extends Queryable2<V1, V2> {
  default Tuple2<V1, V2> peek(final Consumer2<? super V1, ? super V2> peek) {
    nonNullable(peek, "peek");
    return () -> {
      Tuple2<V1, V2> result = Cursor2::none;
      for (final var tuple : this) {
        tuple.as((v1, v2) -> {
          peek.accept(v1, v2);
          return Tuple.of(v1, v2);
        });
        result = tuple.as(Tuple::of);
      }
      return result.iterator();
    };
  }
}
