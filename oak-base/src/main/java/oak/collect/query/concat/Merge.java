package oak.collect.query.concat;

import io.ibex.collect.seq.Sequence;
import oak.collect.query.Queryable;

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
    var seq = Sequence.<S>empty();
    for (final var it : source) seq = seq.add(it);
    for (final var many : seconds) {
      for (final var it : many) {
        seq = seq.add(it);
      }
    }
    return seq.iterator();
  }
}
