package oak.query.sort;

import oak.collect.cursor.Cursor;
import oak.func.fun.Function1;
import oak.query.Queryable;

import java.util.Comparator;
import java.util.Iterator;

final class OrderBy<S, K> implements Sorting<S> {
  private final Queryable<S> many;
  private final Function1<S, K> key;
  private final Comparator<K> comparison;

  OrderBy(final Queryable<S> many, final Function1<S, K> key, final Comparator<K> comparison) {
    this.many = many;
    this.key = key;
    this.comparison = comparison;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final Iterator<S> iterator() {

    for (final var count : many.count()) {
      final var values = (S[]) new Object[count];
      var index = 0;
      for (final var it : many) {
        values[index] = it;


      }
    }

    return Cursor.none();
  }
}
