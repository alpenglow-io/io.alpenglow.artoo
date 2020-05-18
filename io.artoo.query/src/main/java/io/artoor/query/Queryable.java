package io.artoo.query;

import io.artoo.cursor.Cursor;

import java.util.function.Consumer;

@FunctionalInterface
public interface Queryable<R extends Record> extends Iterable<R> {
  default void eventually(final Consumer<R> eventually) {
    for (final var value : this) if (value != null) eventually.accept(value);
  }

  default Cursor<R> cursor() {
    return (Cursor<R>) this.iterator();
  }
}
