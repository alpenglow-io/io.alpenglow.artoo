package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.query.Closure;

public final class NotOfType<T, R> implements Closure<T, T> {
  private final Class<? extends R> type;

  public NotOfType(final Class<? extends R> type) {
    assert type != null;
    this.type = type;
  }

  @Override
  public final T tryApply(final T element) {
    return !type.isInstance(element) ? element : null;
  }
}
