package io.artoo.lance.value;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.One;

public interface Value<T, V extends Value<T, V>> extends Suppl.Uni<T> {
  default One<T> asQueryable() {
    return One.from(this);
  }

  default boolean is(V value) {
    return equals(value);
  }

  T value();

  @Override
  default T tryGet() { return val(); }
}
