package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func.Uni;
import io.artoo.lance.func.Pred.Bi;

public final class Skip<T> implements Uni<T, T> {
  private final Skipped skipped = new Skipped();
  private final Bi<? super Integer, ? super T> where;

  public Skip(final Bi<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return where.tryTest(skipped.index++, element) && ++skipped.count == skipped.index ? null : element;
  }

  private final class Skipped {
    private int index = 0;
    private int count = 0;
  }
}
