package oak.collect.query.partition;

import oak.collect.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

final class Skip<S> implements Partitioning<S, Queryable<S>>, Queryable<S> {
  private final Queryable<S> source;
  private final int until;

  Skip(final Queryable<S> source, final int until) {
    this.source = source;
    this.until = until;
  }

  @Override
  public Iterator<S> iterator() {
    var skip = 0;
    var seq = new ArrayList<S>();
    for (final var it : source) if (skip++ >= until) seq.add(it);
    return seq.iterator();
  }
}
