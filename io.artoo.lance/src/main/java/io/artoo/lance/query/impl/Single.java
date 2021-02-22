package io.artoo.lance.query.impl;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.Uni;

public final class Single<T> implements Func.Uni<T, T> {
  enum NoSingle {Found}

  private Object single = null;
  private final Uni<? super T> where;

  public Single(final Uni<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && single == null) {
      single = element;
    } else if (where.tryTest(element)) {
      single = NoSingle.Found;
    }

    //noinspection unchecked
    return NoSingle.Found.equals(single)
      ? null
      : (T) single;
  }
}
