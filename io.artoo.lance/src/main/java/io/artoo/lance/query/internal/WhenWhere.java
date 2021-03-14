package io.artoo.lance.query.internal;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class WhenWhere<T> implements Func.Uni<T, T> {
  private final Pred.Uni<? super T> where;
  private final Cons.Uni<? super T> cons;

  public WhenWhere(final Pred.Uni<? super T> where, final Cons.Uni<? super T> cons) {
    this.where = where;
    this.cons = cons;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) cons.tryAccept(element);
    return element;
  }
}
