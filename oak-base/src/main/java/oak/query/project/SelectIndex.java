package oak.query.project;

import oak.query.Queryable;
import oak.query.project.Projection.IndexFunction1;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

final class SelectIndex<S, R> implements Queryable<R> {
  private final Queryable<S> some;
  private final IndexFunction1<S, R> indexMap;

  SelectIndex(final Queryable<S> some, final IndexFunction1<S, R> indexMap) {
    this.some = some;
    this.indexMap = indexMap;
  }

  @Override
  public final Iterator<R> iterator() {
    final var idx = new AtomicInteger(0);
    return some
      .select(it -> indexMap.apply(idx.getAndIncrement(), it))
      .iterator();
  }
}
