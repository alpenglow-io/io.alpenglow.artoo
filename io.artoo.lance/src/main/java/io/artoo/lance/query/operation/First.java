package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.func.Pred.Uni;

public final class First<T> implements Func.Uni<T, T> {
  private final Uni<? super T> where;
  private final Found found;

  public First(final Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.found = new Found();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return (found.value |= where.tryTest(element)) ? element : null;
  }

  private final class Found {
    private boolean value = false;
  }
}
