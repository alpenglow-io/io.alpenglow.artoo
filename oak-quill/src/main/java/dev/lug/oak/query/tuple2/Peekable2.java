package dev.lug.oak.query.tuple2;

import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.query.Queryable2;
import dev.lug.oak.query.Tuple2;

import static dev.lug.oak.type.Nullability.nonNullable;

public interface Peekable2<V1, V2> extends Queryable2<V1, V2> {
  default Tuple2<V1, V2> peek(final Consumer2<? super V1, ? super V2> peek) {
    nonNullable(peek, "peek");
    return this.eventually((v1, v2) -> { peek.apply(v1, v2); return this; }, this)::iterator;
  }
}
