package dev.lug.oak.collect;

import dev.lug.oak.func.as.As2;
import dev.lug.oak.func.con.Consumer2;

import java.util.Objects;

public interface Iterable2<V1, V2> extends Iterable<As2<V1, V2>> {
  default void forEach(Consumer2<? super V1, ? super V2> action) {
    Objects.requireNonNull(action);
    for (final var tuple : this) tuple.as(action::apply);
  }
}

