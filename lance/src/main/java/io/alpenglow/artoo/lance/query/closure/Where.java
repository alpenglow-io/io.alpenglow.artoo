package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.scope.Expectation;

public final class Where<T> implements Closure<T, T>, Expectation {
  private final TryPredicate2<? super Integer, ? super T> where;
  private int index;

  public Where(final TryPredicate2<? super Integer, ? super T> predicate) {
    this.where = predicate;
    this.index = 0;
  }

  @Override
  public T invoke(final T element) throws Throwable {
    return where.invoke(index++, element) ? element : null;
  }
}
