package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
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
    if (where.tryTest(element) && found.value == null) {
      found.value = element;
    }

    return found.value;
  }

  private final class Found {
    private T value;
  }
}
