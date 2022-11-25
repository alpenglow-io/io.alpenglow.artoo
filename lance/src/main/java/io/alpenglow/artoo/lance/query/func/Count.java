package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TryPredicate;

public final class Count<T> implements TryFunction<T, Integer> {
  private static final class Counted { private int value = 0; }

  private final Counted counted;
  private final TryPredicate<? super T> where;

  public Count(final TryPredicate<? super T> where) {
    assert where != null;
    this.counted = new Counted();
    this.where = where;
  }

  @Override
  public Integer tryApply(final T element) throws Throwable {
    return element != null && where.tryTest(element) ? ++counted.value : counted.value;
  }
}
