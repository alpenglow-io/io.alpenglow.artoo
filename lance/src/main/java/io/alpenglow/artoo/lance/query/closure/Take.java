package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Closure;

@SuppressWarnings("unchecked")
public final class Take<T, R> implements Closure<T, R> {
  private final Taken taken = new Taken();
  private final TryPredicate2<? super Integer, ? super T> where;

  public Take(final TryPredicate2<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return (R) ((taken.keep = where.invoke(taken.index++, element) && taken.keep) ? element : null);
  }

  private final class Taken {
    private int index = 0;
    private boolean keep = true;
  }
}
