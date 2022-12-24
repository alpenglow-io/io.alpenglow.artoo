package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Closure;

public final class Take<T> implements Closure<T, T> {
  private final TryPredicate2<? super Integer, ? super T> where;
  private int index;
  private boolean keep;

  public Take(final TryPredicate2<? super Integer, ? super T> predicate) {
    this.where = predicate;
    this.index = 0;
    this.keep = true;
  }

  @Override
  public T invoke(final T element) throws Throwable {
    return (keep &= where.invoke(index++, element)) ? element : null;
  }
}
