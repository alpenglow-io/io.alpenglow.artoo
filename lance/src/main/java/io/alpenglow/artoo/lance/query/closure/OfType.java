package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.query.Closure;

public final class OfType<T, R> implements Closure<T, R> {
  private final Class<? extends R> type;

  public OfType(final Class<? extends R> type) {
    assert type != null;
    this.type = type;
  }

  @Override
  public final R tryApply(final T element) {
    return type.isInstance(element) ? type.cast(element) : null;
  }
}
