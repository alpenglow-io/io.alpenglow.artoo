package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

public final class Some<T> implements Closure<T, Boolean> {
  private final TryPredicate1<? super T> predicate;

  public Some(final TryPredicate1<? super T> predicate) {
    this.predicate = predicate;
  }

  @Override
  public Boolean invoke(final T element) throws Throwable {
    return element != null && predicate.invoke(element);
  }
}
