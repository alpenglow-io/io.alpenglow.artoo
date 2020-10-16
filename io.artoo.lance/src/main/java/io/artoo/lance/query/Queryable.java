package io.artoo.lance.query;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.func.Cons;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Queryable<T> extends Iterable<T> {
  Cursor<T> cursor();

  @NotNull
  @Override
  default Iterator<T> iterator() {
    try {
      return cursor().close();
    } catch (Throwable cause) {
      return Cursor.nothing();
    }
  }

  default void eventually(final Cons.Uni<T> eventually) {
    for (final var value : this) {
      if (value != null) {
        eventually.accept(value);
      }
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  default void eventually() {
    for (final var value : this);
  }
}
