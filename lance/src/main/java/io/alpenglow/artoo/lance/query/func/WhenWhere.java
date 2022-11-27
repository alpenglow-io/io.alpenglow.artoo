package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryPredicate1;

public final class WhenWhere<T> implements TryFunction1<T, T> {
  private final TryPredicate1<? super T> where;
  private final TryConsumer1<? super T> cons;

  public WhenWhere(final TryPredicate1<? super T> where, final TryConsumer1<? super T> cons) {
    this.where = where;
    this.cons = cons;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) cons.tryAccept(element);
    return element;
  }
}
