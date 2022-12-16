package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Closure;

public final class Skip<T> implements Closure<T, T> {
  private final TryPredicate2<? super Integer, ? super T> predicate;
  private int index;
  private int count;

  public Skip(final TryPredicate2<? super Integer, ? super T> predicate) {
    this.predicate = predicate;
    this.index = 0;
    this.count = 0;
  }

  @Override
  public T invoke(final T element) throws Throwable {
    return predicate.invoke(index++, element) && ++count == index ? null : element;
  }
}
