package lance;

import lance.func.Cons;
import lance.func.Func;
import lance.func.Recursive;
import lance.func.Recursive.Tailrec;
import lance.literator.Cursor;
import lance.tuple.Pair;

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
    for (final var ignored : this)
      ;
  }

  default <Z> Z eventually(Z returning) {
    eventually();
    return returning;
  }

  default <R, F extends Tailrec<T, R, F> & Recursive<T, R, F>> Func.MaybeFunction<T, R> rec(final F tailrec) {
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

    default <R, F extends Tailrec<Pair<A, B>, R, F> & Recursive<Pair<A, B>, R, F>> Func.MaybeFunction<Pair<A, B>, R> rec(final F tailrec) {
      return element -> tailrec.on(element).result();
    }
  }
}
