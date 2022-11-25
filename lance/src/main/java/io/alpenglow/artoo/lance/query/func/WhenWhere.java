package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryConsumer;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TryPredicate;

public final class WhenWhere<T> implements TryFunction<T, T> {
  private final TryPredicate<? super T> where;
  private final TryConsumer<? super T> cons;

  public WhenWhere(final TryPredicate<? super T> where, final TryConsumer<? super T> cons) {
    this.where = where;
    this.cons = cons;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) cons.tryAccept(element);
    return element;
  }
}
