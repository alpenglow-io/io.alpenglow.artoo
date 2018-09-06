package oak.collect.query.aggregate.mean;

import java.util.Objects;

final class HashCodeMean<M> implements Mean {
  private final Iterable<M> some;

  HashCodeMean(final Iterable<M> some) {
    this.some = some;
  }

  @Override
  public final long get() {
    long size = 0;
    long hashCode = 0;
    for (final var it : some) {
      hashCode += Objects.hashCode(it);
      size++;
    }
    return hashCode / size;
  }
}
