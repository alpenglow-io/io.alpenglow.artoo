package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryPredicate1;

public final class Some<T> implements TryFunction1<T, Boolean> {
  private final TryPredicate1<? super T> where;

  public Some(final TryPredicate1<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public final Boolean tryApply(final T element) throws Throwable {
    return element != null && where.tryTest(element);
  }
}
