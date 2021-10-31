package io.artoo.lance.query.func;

import io.artoo.lance.func.Func;

public final class WhenType<T, R> implements Func.MaybeFunction<T, T> {
  private final Class<R> type;
  private final Func.MaybeFunction<? super R, ? extends T> func;

  public WhenType(final Class<R> type, final Func.MaybeFunction<? super R, ? extends T> func) {
    this.type = type;
    this.func = func;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return type.isInstance(element) ? func.tryApply(type.cast(element)) : element;
  }
}
