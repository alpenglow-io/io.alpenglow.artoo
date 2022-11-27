package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction1;

public final class At<T> implements TryFunction1<T, T> {
  private final int at;
  private final Pointed pointed;

  public At(final int at) {
    this.at = at;
    this.pointed = new Pointed();
  }

  @Override
  public T tryApply(T element) {
    return pointed.index++ == at ? element : null;
  }

  private static final class Pointed {
    private int index = 0;
  }
}
