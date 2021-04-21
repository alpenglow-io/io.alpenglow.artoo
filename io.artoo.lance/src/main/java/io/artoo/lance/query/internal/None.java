package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.Uni;

public final class None<T> implements Func.Uni<T, Boolean> {
  private final Uni<? super T> where;
  private final NoneOfThem noneOfThem;

  public None(final Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.noneOfThem = new NoneOfThem();
  }

  @Override
  public Boolean tryApply(final T element) throws Throwable {
    return (noneOfThem.value &= !where.tryTest(element));
  }

  private static final class NoneOfThem {
    private boolean value = true;
  }
}
