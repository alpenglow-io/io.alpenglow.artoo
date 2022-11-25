package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TryPredicate;

public final class Some<T> implements TryFunction<T, Boolean> {
  private final TryPredicate<? super T> where;

  public Some(final TryPredicate<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public final Boolean tryApply(final T element) throws Throwable {
    return element != null && where.tryTest(element);
  }
}
