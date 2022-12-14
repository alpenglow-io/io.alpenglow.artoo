package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryConsumer2;
import io.alpenglow.artoo.lance.query.Closure;

@SuppressWarnings("unchecked")
public final class Peek<T, R> implements Closure<T, R> {
  private final Peeked peeked = new Peeked();
  private final TryConsumer2<? super Integer, ? super T> peek;

  public Peek(final TryConsumer2<? super Integer, ? super T> peek) {
    assert peek != null;
    this.peek = peek;
  }

  @Override
  public final R tryApply(final T element) throws Throwable {
    peek.invoke(peeked.index++, element);
    return (R) element;
  }

  private static final class Peeked {
    private int index;
  }
}
