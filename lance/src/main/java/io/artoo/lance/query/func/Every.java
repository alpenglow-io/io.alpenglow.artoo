package io.artoo.lance.query.func;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.MaybePredicate;

public final class Every<T> implements Func.MaybeFunction<T, Boolean> {
  private final MaybePredicate<? super T> where;
  private final AllOfThem allOfThem;

  public Every(final MaybePredicate<? super T> where) {
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
