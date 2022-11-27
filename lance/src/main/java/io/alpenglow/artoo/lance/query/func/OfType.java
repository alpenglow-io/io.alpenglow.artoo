package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction1;

public final class OfType<T, R> implements TryFunction1<T, R> {
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
