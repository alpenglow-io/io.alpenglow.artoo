package io.artoo.query.many.internal;

import io.artoo.collect.$2.Iterable;
import io.artoo.collect.$2.Iterator;
import io.artoo.cursor.$2.Cursor;
import io.artoo.func.$2.Pred;
import io.artoo.query.Many;
import io.artoo.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class Having<K, T> implements io.artoo.query.$2.Many<K, Many<T>> {
  private final io.artoo.collect.$2.Iterable<K, Many<T>> iterable;
  private final Pred<? super K, ? super Many<T>> having;

  @Contract(pure = true)
  public Having(final Iterable<K, Many<T>> iterable, final Pred<? super K, ? super Many<T>> having) {
    this.iterable = iterable;
    this.having = having;
  }

  @NotNull
  @Override
  public Iterator<K, Many<T>> iterator() {
    final var result = new ArrayList<Union<K, Many<T>>>();
    for (final var union : iterable) {
      if (union.as(having::test)) {
        result.add(union);
      }
    }
    return Cursor.from(result.iterator());
  }
}
