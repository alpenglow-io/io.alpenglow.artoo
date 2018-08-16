package oak.collect.query.partition;

import oak.collect.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

final class Take<S> implements Queryable<S> {
  private final Queryable<S> source;
  private final int until;

  Take(final Queryable<S> source, final int until) {
    this.source = source;
    this.until = until;
  }

  @Override
  public final Iterator<S> iterator() {
    var take = 0;
    var seq = new ArrayList<S>();
    for (final var it : source) if (take++ < until) seq.add(it);
    return seq.iterator();
  }
}
