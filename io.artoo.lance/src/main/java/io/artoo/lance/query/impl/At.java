package io.artoo.lance.query.impl;

import io.artoo.lance.func.Func.Uni;

public final class At<T> implements Uni<T, T> {
  private final int at;
  private final Pointed pointed;

  public At(final int at) {
    this.at = at;
    this.pointed = new Pointed();
  }

  @Override
  public final T tryApply(final T element) {
    return pointed.index++ == at ? element : null;
  }

  private final class Pointed {
    private int index = 0;
  }
}
