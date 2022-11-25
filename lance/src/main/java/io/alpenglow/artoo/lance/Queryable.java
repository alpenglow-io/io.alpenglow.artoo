package io.alpenglow.artoo.lance;

import io.alpenglow.artoo.lance.func.Recursive;
import io.alpenglow.artoo.lance.func.Recursive.Tailrec;
import io.alpenglow.artoo.lance.func.TryBiConsumer;
import io.alpenglow.artoo.lance.func.TryConsumer;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.tuple.Pair;

import java.util.Iterator;

public interface Queryable<T> extends Iterable<T> {
  Cursor<T> cursor();

  @Override
  default Iterator<T> iterator() {
    try {
      return cursor().close();
    } catch (Throwable cause) {
      cause.printStackTrace();
      return Cursor.nothing();
    }
  }

  default void eventually(final TryConsumer<T> eventually) {
    for (final var value : this) {
      if (value != null) {
        eventually.accept(value);
      }
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  default void eventually() {
    for (final var ignored : this)
      ;
  }

  default <Z> Z eventually(Z returning) {
    eventually();
    return returning;
  }

  default <R, F extends Tailrec<T, R, F> & Recursive<T, R, F>> TryFunction<T, R> rec(final F tailrec) {
    return element -> tailrec.on(element).result();
  }

  interface OfTwo<A, B> extends Iterable<Pair<A, B>> {
    Cursor<Pair<A, B>> cursor();

    @Override
    default Iterator<Pair<A, B>> iterator() {
      try {
        return cursor().close();
      } catch (Throwable cause) {
        cause.printStackTrace();
        return Cursor.nothing();
      }
    }

    default void eventually(final TryBiConsumer<A, B> eventually) {
      for (final var tuple : this) {
        if (tuple != null) {
          tuple.peek(eventually);
        }
      }
    }

    @SuppressWarnings("UnnecessaryContinue")
    default void eventually() {
      for (final var value : this) {
        continue;
      }
    }

    default <R, F extends Tailrec<Pair<A, B>, R, F> & Recursive<Pair<A, B>, R, F>> TryFunction<Pair<A, B>, R> rec(final F tailrec) {
      return element -> tailrec.on(element).result();
    }
  }
}
