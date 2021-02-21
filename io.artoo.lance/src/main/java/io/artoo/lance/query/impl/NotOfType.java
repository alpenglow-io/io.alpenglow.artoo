package io.artoo.lance.query.impl;

import io.artoo.lance.func.Func;

public final class NotOfType<T, R> implements Func.Uni<T, T> {
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
