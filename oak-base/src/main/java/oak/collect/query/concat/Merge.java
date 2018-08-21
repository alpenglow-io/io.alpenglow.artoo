package oak.collect.query.concat;

import oak.collect.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

final class Merge<S> implements Concatenation<S, Queryable<S>>, Queryable<S> {
  private final Queryable<S> source;
  private final Queryable<S>[] seconds;

  Merge(final Queryable<S> source, final Queryable<S>[] seconds) {
    this.source = source;
    this.seconds = seconds;
  }

  @Override
  public final Iterator<S> iterator() {
    var seq = new ArrayList<S>();
    for (final var it : source) seq.add(it);
    for (final var many : seconds) {
      for (final var it : many) {
        seq.add(it);
      }
    }
    return seq.iterator();
  }
}
