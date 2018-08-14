package oak.query.partition;

import io.ibex.collect.seq.Sequence;
import oak.query.Queryable;

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
    var seq = Sequence.<S>empty();
    for (final var it : source) if (skip++ >= until) seq = seq.add(it);
    return seq.iterator();
  }
}
