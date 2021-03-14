package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.Bi;

@SuppressWarnings("unchecked")
public final class Skip<T, R> implements Func.Uni<T, R> {
  private final Skipped skipped = new Skipped();
  private final Bi<? super Integer, ? super T> where;

  public Skip(final Bi<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return (R) (where.tryTest(skipped.index++, element) && ++skipped.count == skipped.index ? null : element);
  }

  private final class Skipped {
    private int index = 0;
    private int count = 0;
  }
}
