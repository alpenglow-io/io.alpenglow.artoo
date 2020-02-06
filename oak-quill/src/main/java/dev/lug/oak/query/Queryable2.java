package dev.lug.oak.query;

import dev.lug.oak.collect.Iterable2;
import dev.lug.oak.collect.cursor.Cursor2;
import dev.lug.oak.func.as.As2;
import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.func.fun.Function2;

public interface Queryable2<V1, V2> extends Iterable2<V1, V2> {
  default void eventually(final Consumer2<V1, V2> consumer) {
    for (final var tuple : this) tuple.as(consumer);
  }
}
