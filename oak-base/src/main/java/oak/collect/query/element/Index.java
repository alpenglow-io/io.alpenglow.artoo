package oak.collect.query.element;

import oak.collect.query.Maybe;
import oak.func.sup.AsLong;

final class Index implements AsLong {
  private final long value;

  private Index(long value) {
    this.value = value;
  }

  @Override
  public final long get() {
    return this.value;
  }

  static Maybe<Index> of(final long value) {
    return Maybe.just(value)
      .where(it -> it >= 0)
      .select(Index::new);
  }
}
