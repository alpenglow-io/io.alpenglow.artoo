package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.query.Unit;

public final class At<T> implements Closure<T, T> {
  private final int at;
  private final Pointed pointed;

  public At(final int at) {
    this.at = at;
    this.pointed = new Pointed();
  }

  @Override
  public Unit<T> invoke(Unit<T> element) throws Throwable {
    return pointed.index++ == at ? element : Unit.nothing();
  }

  private static final class Pointed {
    private int index = 0;
  }
}
