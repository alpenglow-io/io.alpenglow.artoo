package dev.lug.oak.collect.$2;

import dev.lug.oak.union.$2.Union;
import dev.lug.oak.func.$2.Con;

import java.util.Objects;

public interface Iterable<V1, V2> extends java.lang.Iterable<Union<V1, V2>> {
  default void forEach(Con<? super V1, ? super V2> action) {
    Objects.requireNonNull(action);
    for (final var tuple : this) tuple.as(action::apply);
  }
}

