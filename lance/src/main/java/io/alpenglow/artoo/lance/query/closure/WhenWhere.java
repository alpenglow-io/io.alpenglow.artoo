package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

public final class WhenWhere<T> implements Closure<T, T> {
  private final TryPredicate1<? super T> predicate;
  private final TryConsumer1<? super T> consumer;

  public WhenWhere(final TryPredicate1<? super T> predicate, final TryConsumer1<? super T> consumer) {
    this.predicate = predicate;
    this.consumer = consumer;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (predicate.invoke(element)) consumer.invoke(element);
    return element;
  }
}
