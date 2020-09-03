package io.artoo.lance.query;

import io.artoo.lance.func.Cons;
import io.artoo.lance.next.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface Queryable<R> extends Iterable<R> {
  default void eventually(final Cons.Uni<R> eventually) {
    for (final var value : this) if (value != null) eventually.accept(value);
  }

  Cursor<R> cursor();

  @Override
  default @NotNull Iterator<R> iterator() {
    try {
      return cursor();
    } catch (Throwable error) {
      return Cursor.nothing();
    }
  }
}
