package oak.collect.$3;

import oak.func.$3.Cons;
import oak.union.$3.Union;

import java.util.Objects;

public interface Iterable<V1, V2, V3> extends java.lang.Iterable<Union<V1, V2, V3>> {
  default void forEach(Cons<? super V1, ? super V2, ? super V3> action) {
    Objects.requireNonNull(action);
    for (final var tuple : this) tuple.as(action::apply);
  }
}
