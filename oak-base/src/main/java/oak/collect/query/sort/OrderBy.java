package oak.collect.query.sort;

import oak.collect.cursor.Cursor;
import oak.func.fun.Function1;
import oak.collect.query.Queryable;

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
    return Cursor.none();
  }
}
