package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryFunction;

@SuppressWarnings("unchecked")
public final class Skip<T, R> implements TryFunction<T, R> {
  private final Skipped skipped = new Skipped();
  private final TryBiPredicate<? super Integer, ? super T> where;

  public Skip(final TryBiPredicate<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return (R) (where.tryTest(skipped.index++, element) && ++skipped.count == skipped.index ? null : element);
  }

  private final class Skipped {
    private int index = 0;
    private int count = 0;
  }
}
