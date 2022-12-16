package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Closure;

public final class WhenType<T, R> implements Closure<T, T> {
  private final Class<R> type;
  private final TryFunction1<? super R, ? extends T> func;

  public WhenType(final Class<R> type, final TryFunction1<? super R, ? extends T> func) {
    this.type = type;
    this.func = func;
  }

  @Override
  public T invoke(final T element) throws Throwable {
    return type.isInstance(element) ? func.invoke(type.cast(element)) : element;
  }
}
