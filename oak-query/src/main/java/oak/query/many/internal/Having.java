package oak.query.many.internal;

import oak.collect.$2.Iterable;
import oak.collect.$2.Iterator;
import oak.cursor.$2.Cursor;
import oak.func.$2.Pred;
import oak.query.Many;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class Having<K, T> implements oak.query.$2.Many<K, Many<T>> {
  private final oak.collect.$2.Iterable<K, Many<T>> iterable;
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
