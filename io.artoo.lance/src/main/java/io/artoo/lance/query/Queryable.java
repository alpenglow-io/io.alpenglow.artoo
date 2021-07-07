package io.artoo.lance.query;

import io.artoo.lance.func.Cons;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.tuple.Pair;
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
      cause.printStackTrace();
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

  default <Z> Z eventually(Z returning) {
    eventually();
    return returning;
  }

  interface OfTwo<A, B> extends Iterable<Pair<A, B>> {
    Cursor<Pair<A, B>> cursor();

    @NotNull
    @Override
    default Iterator<Pair<A, B>> iterator() {
      try {
        return cursor().close();
      } catch (Throwable cause) {
        cause.printStackTrace();
        return Cursor.nothing();
      }
    }

    default void eventually(final Cons.Bi<A, B> eventually) {
      for (final var tuple : this) {
        if (tuple != null) {
          tuple.peek(eventually);
        }
      }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    default void eventually() {
      for (final var value : this);
    }
  }
}
