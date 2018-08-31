package oak.collect.query.aggregate;

import oak.collect.query.Queryable;
import oak.func.fun.Function2;

import static java.util.Objects.isNull;

final class Accumulate<T> implements Aggregation<T> {
  private final Queryable<T> sequence;
  private final Function2<T, T, T> accumulate;

  Accumulate(final Queryable<T> sequence, final Function2<T, T, T> accumulate) {
    this.sequence = sequence;
    this.accumulate = accumulate;
  }

  @Override
  public final T get() {
    T reduced = null;
    for (final var s : sequence) reduced = isNull(reduced) ? s : accumulate.apply(reduced, s);
    return reduced;
  }

  @Override
  public String toString() {
    return String.format("Accumulate{sequence=%s, accumulate=%s}", sequence, accumulate);
  }
}
