package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.func.TryFunction1;

public final class Where<T> implements TryFunction1<T, T> {
  private final class Index {
    private int value = 0;
  }

  private final TryPredicate2<? super Integer, ? super T> where;
  private final Index index;

  public Where(final TryPredicate2<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
    this.index = new Index();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return where.tryTest(index.value++, element) ? element : null;
  }
}
