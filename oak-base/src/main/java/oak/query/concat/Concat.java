package oak.query.concat;

import io.ibex.collect.seq.Sequence;
import oak.query.Queryable;

import java.util.Iterator;

final class Concat<S> implements Concatenation<S, Queryable<S>>, Queryable<S> {
  private final Queryable<S> source;
  private final S[] values;

  @SafeVarargs
  Concat(final Queryable<S> source, final S... values) {
    this.source = source;
    this.values = values;
  }

  @Override
  public final Iterator<S> iterator() {
    var seq = Sequence.<S>empty();
    for (final var it : source) seq = seq.add(it);
    for (final var it : values) seq = seq.add(it);
    return seq.iterator();
  }
}
