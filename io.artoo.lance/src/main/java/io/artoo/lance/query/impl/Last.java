package io.artoo.lance.query.impl;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class Last<T> implements Func.Uni<T, T> {
  private final Pred.Uni<? super T> where;
  private final Found found;

  public Last(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.found = new Found();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) {
      found.value = element;
    }
    return found.value;
  }

  private final class Found {
    private T value;
  }
}
