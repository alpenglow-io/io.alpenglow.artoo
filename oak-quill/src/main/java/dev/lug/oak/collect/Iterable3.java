package dev.lug.oak.collect;

import dev.lug.oak.func.as.As3;
import dev.lug.oak.func.con.Consumer3;

import java.util.Objects;

public interface Iterable3<V1, V2, V3> extends Iterable<As3<V1, V2, V3>> {
  default void forEach(Consumer3<? super V1, ? super V2, ? super V3> action) {
    Objects.requireNonNull(action);
    for (final var tuple : this) tuple.as(action::apply);
  }
}
