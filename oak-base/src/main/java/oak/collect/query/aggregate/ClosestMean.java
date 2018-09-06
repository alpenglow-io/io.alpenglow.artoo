package oak.collect.query.aggregate;

import oak.collect.query.aggregate.mean.Mean;
import oak.func.sup.Supplier1;

import static java.util.Objects.requireNonNull;

final class ClosestMean<M> implements Supplier1<M> {
  private final Iterable<M> some;
  private final Mean mean;

  private ClosestMean(final Iterable<M> some, final Mean mean) {
    this.some = some;
    this.mean = mean;
  }

  @Override
  public final M get() {
    final var hashCode = mean.get();
    M found = null;
    for (final var it : some) found = it.hashCode() <= hashCode ? it : found;
    return found;
  }

  public static <C> ClosestMean<C> of(final Iterable<C> some, final Mean mean) {
    return new ClosestMean<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(mean , "Mean is null")
    );
  }
}
