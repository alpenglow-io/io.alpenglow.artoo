package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

public final class Single<T> implements Closure<T, T> {
  private final TryPredicate1<? super T> predicate;
  private T single;
  private boolean moreThanOne;

  public Single(final TryPredicate1<? super T> predicate) {
    this.predicate = predicate;
    this.single = null;
    this.moreThanOne = false;
  }

  @Override
  public T invoke(final T element) throws Throwable {
    if (predicate.invoke(element) && single == null && !moreThanOne) {
      single = element;
    } else if (predicate.invoke(element) && single != null && !moreThanOne) {
      single = null;
      moreThanOne = true;
    }
    return single;
  }
}
