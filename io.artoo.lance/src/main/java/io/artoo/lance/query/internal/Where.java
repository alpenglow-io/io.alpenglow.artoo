package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class Where<T> implements Func.Uni<T, T> {
  private final class Index {
    private int value = 0;
  }

  private final Pred.Bi<? super Integer, ? super T> where;
  private final Index index;

  public Where(final Pred.Bi<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
    this.index = new Index();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return where.tryTest(index.value++, element) ? element : null;
  }
}
