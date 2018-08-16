package oak.collect.query.project;

import oak.collect.query.Queryable;
import oak.func.fun.Function1;

import java.util.ArrayList;
import java.util.Iterator;

final class SelectMany<T, R> implements Queryable<R> {
  private final Queryable<T> some;
  private final Function1<T, R> map;

  SelectMany(final Queryable<T> some, final Function1<T, R> map) {
    this.some = some;
    this.map = map;
  }

  @Override
  public final Iterator<R> iterator() {
    var mapped = new ArrayList<R>();
    for (final var value : some) {
      mapped.add(map.apply(value));
    }
    return mapped.iterator();
  }
}
