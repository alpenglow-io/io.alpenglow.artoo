package io.artoo.lance.query.func;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class WhenWhere<T> implements Func.MaybeFunction<T, T> {
  private final Pred.MaybePredicate<? super T> where;
  private final Cons.MaybeConsumer<? super T> cons;

  public WhenWhere(final Pred.MaybePredicate<? super T> where, final Cons.MaybeConsumer<? super T> cons) {
    this.where = where;
    this.cons = cons;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) cons.tryAccept(element);
    return element;
  }
}
