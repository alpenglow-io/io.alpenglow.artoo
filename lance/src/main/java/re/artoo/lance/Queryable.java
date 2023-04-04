package re.artoo.lance;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;

import java.util.Iterator;

public interface Queryable<T> extends Iterable<T> {
  Cursor<T> cursor();

  @Override
  default Iterator<T> iterator() {
    try {
      return cursor();
    } catch (Throwable throwable) {
      throw new Cursor.Exception("Can't run cursor, since %s".formatted(throwable.getMessage()), throwable);
    }
  }

  default void eventually(final TryConsumer1<T> eventually) {
    for (var pair : this) {
      eventually.accept(pair);
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  default void eventually() {
    for (final var ignored : this)
      ;
  }

  default <RETURN> RETURN eventually(RETURN returning) {
    eventually();
    return returning;
  }

  interface OfTwo<A, B> extends Iterable<Pair<A, B>> {
    Cursor<Pair<A, B>> cursor();

    @Override
    default Iterator<Pair<A, B>> iterator() {
      try {
        return cursor();
      } catch (Throwable throwable) {
        throw new Cursor.Exception("Can't run cursor, since %s".formatted(throwable.getMessage()), throwable);
      }
    }

    default void eventually(final TryConsumer2<A, B> eventually) {
      for (var pair : this) {
        pair.also(eventually);
      }
    }

    @SuppressWarnings("UnnecessaryContinue")
    default void eventually() {
      for (final var value : this) {
        continue;
      }
    }
  }
}
