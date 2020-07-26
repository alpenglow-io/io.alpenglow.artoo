package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.cursor.Hand;
import io.artoo.lance.func.Cons;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface Queryable<R> extends Iterable<R> {
  default void eventually(final Cons.Uni<R> eventually) {
    for (final var value : this) if (value != null) eventually.accept(value);
  }

  Cursor<R> cursor() throws Throwable;

  @Override
  default @NotNull Iterator<R> iterator() {
    try {
      return cursor().yield();
    } catch (Throwable error) {
      return Cursor.every();
    }
  }

  default Many<R> asMany() {
    return this::cursor;
  }

  default One<R> asOne() {
    return this::cursor;
  }

  default Eventual<R> asEventual() {
    return () -> Hand.of(this.cursor());
  }
}
