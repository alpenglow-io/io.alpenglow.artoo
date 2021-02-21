package io.artoo.lance.query.impl;

import io.artoo.lance.func.Cons.Bi;
import io.artoo.lance.func.Func;

@SuppressWarnings("unchecked")
public final class Peek<T, R> implements Func.Uni<T, R> {
  private final Peeked peeked = new Peeked();
  private final Bi<? super Integer, ? super T> peek;

  public Peek(final Bi<? super Integer, ? super T> peek) {
    assert peek != null;
    this.peek = peek;
  }

  @Override
  public final R tryApply(final T element) throws Throwable {
    peek.tryAccept(peeked.index++, element);
    return (R) element;
  }

  private final class Peeked {
    private int index;
  }
}
