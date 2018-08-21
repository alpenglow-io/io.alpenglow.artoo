package oak.collect.query.concat;

import oak.collect.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Arrays.asList;

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
    var array = new ArrayList<S>();
    for (final var it : source) array.add(it);
    array.addAll(asList(values));
    return array.iterator();
  }
}
