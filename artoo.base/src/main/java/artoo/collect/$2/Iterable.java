package artoo.collect.$2;

import artoo.func.$2.Cons;
import artoo.union.$2.Union;

import java.util.Objects;

public interface Iterable<V1, V2> extends java.lang.Iterable<Union<V1, V2>> {
  default void forEach(Cons<? super V1, ? super V2> action) {
    Objects.requireNonNull(action);
    for (final var tuple : this)
      tuple.as(action::apply);
  }
}

