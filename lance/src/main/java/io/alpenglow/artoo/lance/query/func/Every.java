package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryPredicate;
import io.alpenglow.artoo.lance.func.TryFunction;

public final class Every<T> implements TryFunction<T, Boolean> {
  private final TryPredicate<? super T> where;
  private final AllOfThem allOfThem;

  public Every(final TryPredicate<? super T> where) {
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
