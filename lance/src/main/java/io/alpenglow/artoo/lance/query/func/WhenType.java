package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction1;

public final class WhenType<T, R> implements TryFunction1<T, T> {
  private final Class<R> type;
  private final TryFunction1<? super R, ? extends T> func;

  public WhenType(final Class<R> type, final TryFunction1<? super R, ? extends T> func) {
    this.type = type;
    this.func = func;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return type.isInstance(element) ? func.tryApply(type.cast(element)) : element;
  }
}
