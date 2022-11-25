package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryFunction;

public final class Where<T> implements TryFunction<T, T> {
  private final class Index {
    private int value = 0;
  }

  private final TryBiPredicate<? super Integer, ? super T> where;
  private final Index index;

  public Where(final TryBiPredicate<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
    this.index = new Index();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return where.tryTest(index.value++, element) ? element : null;
  }
}
