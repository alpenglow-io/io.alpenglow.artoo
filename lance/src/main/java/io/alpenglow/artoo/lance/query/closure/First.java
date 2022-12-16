package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.scope.Expectation;

public final class First<T> implements Closure<T, T>, Expectation {
  private final TryPredicate1<? super T> predicate;
  private T first;

  public First(final TryPredicate1<? super T> predicate) {
    this.predicate = predicate;
    this.first = null;
  }

  @Override
  public T invoke(final T element) throws Throwable {
    if (predicate.invoke(element) && first == null) {
      first = element;
    }
    return first;
  }
}
