package dev.lug.oak.query;

import dev.lug.oak.func.Con;

@FunctionalInterface
public interface Queryable<T> extends Iterable<T> {
  default void eventually(final Con<T> eventually) {
    for (final var value : this) eventually.accept(value);
  }
}
