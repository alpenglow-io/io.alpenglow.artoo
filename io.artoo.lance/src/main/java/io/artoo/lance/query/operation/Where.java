package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class Where<T, R> implements Func.Uni<T, R> {
  private final class Index {
    private int value = 0;
  }

  private final Pred.Bi<? super Integer, ? super T> where;
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;
  private final Index index;

  public Where(final Pred.Bi<? super Integer, ? super T> where, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    assert where != null && select != null;
    this.where = where;
    this.select = select;
    this.index = new Index();
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return where.tryTest(index.value++, element) ? select.tryApply(index.value, element) : null;
  }
}
