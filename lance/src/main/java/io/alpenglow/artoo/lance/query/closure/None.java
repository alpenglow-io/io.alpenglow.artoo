package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

public final class None<T> implements Closure<T, Boolean> {
  private final TryPredicate1<? super T> where;
  private final NoneOfThem noneOfThem;

  public None(final TryPredicate1<? super T> where) {
    assert where != null;
    this.where = where;
    this.noneOfThem = new NoneOfThem();
  }

  @Override
  public Boolean tryApply(final T element) throws Throwable {
    return (noneOfThem.value &= !where.invoke(element));
  }

  private static final class NoneOfThem {
    private boolean value = true;
  }
}
