package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func.Uni;
import io.artoo.lance.func.Pred.Bi;

@SuppressWarnings("unchecked")
public final class Take<T, R> implements Uni<T, R> {
  private final Taken taken = new Taken();
  private final Bi<? super Integer, ? super T> where;

  public Take(final Bi<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return (R) ((taken.keep = where.tryTest(taken.index++, element) && taken.keep) ? element : null);
  }

  private final class Taken {
    private int index = 0;
    private boolean keep = true;
  }
}
