package io.artoo.lance.query;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.task.Atomic;
import io.artoo.lance.tuple.Pair;

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

  default void eventually(final Cons.MaybeConsumer<T> eventually) {
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

  default <R, F extends Func.MaybeFunction<T, Tail<T, R, F>>> Func.MaybeFunction<T, R> as(final Atomic<F> atomically) {
    return element -> atomically.let(it -> it.apply(element), Tail::func)
      .orElseThrow(() -> new IllegalStateException("Can't select atomically"))
      .returning();
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

    default void eventually(final Cons.MaybeBiConsumer<A, B> eventually) {
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

    default <R, F extends Func.MaybeFunction<Pair<A, B>, Tail<Pair<A, B>, R, F>>> Func.MaybeFunction<Pair<A, B>, R> as(final Atomic<F> atomically) {
      return element -> atomically.let(it -> it.apply(element), Tail::func)
        .orElseThrow(() -> new IllegalStateException("Can't select atomically"))
        .returning();
    }
  }
}
