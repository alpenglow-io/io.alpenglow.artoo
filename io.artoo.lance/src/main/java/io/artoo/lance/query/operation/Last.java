package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class Last<T> implements Func.Uni<T, T> {
  private final Pred.Uni<? super T> where;

  public Last(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return where.tryTest(element) ? element : null;
  }
}
