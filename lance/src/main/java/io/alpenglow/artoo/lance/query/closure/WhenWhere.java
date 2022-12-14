package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

public final class WhenWhere<T> implements Closure<T, T> {
  private final TryPredicate1<? super T> where;
  private final TryConsumer1<? super T> cons;

  public WhenWhere(final TryPredicate1<? super T> where, final TryConsumer1<? super T> cons) {
    this.where = where;
    this.cons = cons;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.invoke(element)) cons.invoke(element);
    return element;
  }
}
