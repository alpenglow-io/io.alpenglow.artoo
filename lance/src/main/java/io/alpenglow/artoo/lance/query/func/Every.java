package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.func.TryFunction1;

public final class Every<T> implements TryFunction1<T, Boolean> {
  private final TryPredicate1<? super T> where;
  private final AllOfThem allOfThem;

  public Every(final TryPredicate1<? super T> where) {
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
