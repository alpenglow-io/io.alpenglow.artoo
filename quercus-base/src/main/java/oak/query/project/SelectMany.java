package oak.query.project;

import io.ibex.collect.seq.Sequence;
import oak.func.fun.Function1;
import oak.query.Queryable;

import java.util.Iterator;

final class SelectMany<T, R> implements Queryable<R> {
  private final Queryable<T> some;
  private final Function1<T, R> map;

  SelectMany(final Queryable<T> some, final Function1<T, R> map) {
    this.some = some;
    this.map = map;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Iterator<R> iterator() {
    var mapped = Sequence.<R>empty();
    for (final var value : some) {
      mapped = mapped.add(map.apply(value));
    }
    return mapped.iterator();
  }
}
