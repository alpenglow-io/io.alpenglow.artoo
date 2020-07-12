package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class Take<T> implements Func.Uni<T, T> {
  private final Taken taken = new Take.Taken();
  private final Pred.Bi<? super Integer, ? super T> where;

  public Take(final Pred.Bi<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return (taken.keep = where.tryTest(taken.index++, element) && taken.keep) ? element : null;
  }

  private final class Taken {
    private int index = 0;
    private boolean keep = true;
  }
}
