package io.artoo.lance.query.operation;

import io.artoo.lance.func.Cons.Bi;
import io.artoo.lance.func.Func.Uni;

public final class Peek<T> implements Uni<T, T> {
  private final Peeked peeked = new Peeked();
  private final Bi<? super Integer, ? super T> peek;

  public Peek(final Bi<? super Integer, ? super T> peek) {
    assert peek != null;
    this.peek = peek;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    peek.tryAccept(peeked.index++, element);
    return element;
  }

  private final class Peeked {
    private int index;
  }
}
