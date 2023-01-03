package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.query.Closure;

public final class Peek<T> implements Closure<T, T> {
  private int peeked;
  private final TryConsumer2<? super Integer, ? super T> consumer;

  public Peek(final TryConsumer2<? super Integer, ? super T> consumer) {
    this.consumer = consumer;
  }

  @Override
  public T invoke(final T element) throws Throwable {
    consumer.invoke(peeked++, element);
    return element;
  }
}
