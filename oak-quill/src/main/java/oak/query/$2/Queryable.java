package oak.query.$2;

import oak.collect.$2.Iterable;
import oak.func.$2.Cons;

public interface Queryable<V1, V2> extends Iterable<V1, V2> {
  default void eventually(final Cons<V1, V2> consumer) {
    for (final var tuple : this) tuple.as(consumer);
  }
}
