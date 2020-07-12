package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.func.Pred.Uni;

public final class All<T> implements Func.Uni<T, Boolean> {
  private final Uni<? super T> where;
  private final AllOfThem allOfThem;

  public All(final Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.allOfThem = new AllOfThem();
  }

  @Override
  public Boolean tryApply(final T element) throws Throwable {
    return (allOfThem.value &= where.tryApply(element));
  }

  private final class AllOfThem {
    private boolean value = true;
  }
}
