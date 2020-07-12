package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;

public final class At<T> implements Func.Uni<T, T> {
  private final int at;
  private final At.Pointed pointed;

  public At(final int at) {
    this.at = at;
    this.pointed = new At.Pointed();
  }

  @Override
  public T tryApply(final T element) {
    return pointed.index++ == at ? element : null;
  }

  private final class Pointed {
    private int index = 0;
  }
}
