package lance.query.func;

import lance.func.Func;

public final class WhenType<T, R> implements Func.TryFunction<T, T> {
  private final Class<R> type;
  private final Func.TryFunction<? super R, ? extends T> func;

  public WhenType(final Class<R> type, final Func.TryFunction<? super R, ? extends T> func) {
    this.type = type;
    this.func = func;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return type.isInstance(element) ? func.tryApply(type.cast(element)) : element;
  }
}
