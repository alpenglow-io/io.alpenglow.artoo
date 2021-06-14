package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.Uni;

public final class Every<T> implements Func.Uni<T, Boolean> {
  private final Uni<? super T> where;
  private final AllOfThem allOfThem;

  public Every(final Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.allOfThem = new AllOfThem();
  }

  @Override
  public Boolean tryApply(final T element) throws Throwable {
    return (allOfThem.value &= where.tryTest(element));
  }

  private static final class AllOfThem {
    private boolean value = true;
  }
}
