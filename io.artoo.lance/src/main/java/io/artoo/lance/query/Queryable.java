package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;

import java.util.function.Consumer;

@FunctionalInterface
public interface Queryable<R> extends Iterable<R> {
  default void eventually(final Consumer<R> eventually) {
    for (final var value : this) if (value != null) eventually.accept(value);
  }
}