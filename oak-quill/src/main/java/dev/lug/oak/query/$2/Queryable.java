package dev.lug.oak.query.$2;

import dev.lug.oak.collect.$2.Iterable;
import dev.lug.oak.func.$2.Con;

public interface Queryable<V1, V2> extends Iterable<V1, V2> {
  default void eventually(final Con<V1, V2> consumer) {
    for (final var tuple : this) tuple.as(consumer);
  }
}
