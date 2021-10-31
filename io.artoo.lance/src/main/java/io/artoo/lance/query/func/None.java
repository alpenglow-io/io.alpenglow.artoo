package io.artoo.lance.query.func;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.MaybePredicate;

public final class None<T> implements Func.MaybeFunction<T, Boolean> {
  private final MaybePredicate<? super T> where;
  private final NoneOfThem noneOfThem;

  public None(final MaybePredicate<? super T> where) {
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
