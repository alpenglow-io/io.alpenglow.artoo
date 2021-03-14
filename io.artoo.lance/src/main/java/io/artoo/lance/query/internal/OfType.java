package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;

public final class OfType<T, R> implements Func.Uni<T, R> {
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
