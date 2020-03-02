package oak.query.many;

import oak.collect.$2.Iterable;
import oak.collect.$2.Iterator;
import oak.cursor.$2.Cursor;
import oak.func.$2.Pred;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Grouping<K, T> extends oak.collect.$2.Iterable<K, Many<T>> {
  default oak.query.many.$2.Many<K, Many<T>> having(final Pred<? super K, ? super Many<T>> having) {
    return new Having<>(this, nonNullable(having, "having"));
  }
}

final class Having<K, T> implements oak.query.many.$2.Many<K, Many<T>> {
  private final oak.collect.$2.Iterable<K, Many<T>> iterable;
  private final Pred<? super K, ? super Many<T>> having;

  @Contract(pure = true)
  Having(final Iterable<K, Many<T>> iterable, final Pred<? super K, ? super Many<T>> having) {
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
