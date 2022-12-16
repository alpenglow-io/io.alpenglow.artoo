package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

public final class Last<T> implements Closure<T, T> {
  private final TryPredicate1<? super T> predicate;
  private T last;

  public Last(final TryPredicate1<? super T> predicate) {
    this.predicate = predicate;
    this.last = null;
  }
  @Override
  public T invoke(T element) throws Throwable {
    if (predicate.invoke(element)) last = element;
    return last;
  }
}
