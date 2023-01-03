package re.artoo.lance.query.closure;

import re.artoo.lance.query.Closure;

public final class NotOfType<T, R> implements Closure<T, T> {
  private final Class<? extends R> type;

  public NotOfType(final Class<? extends R> type) {
    this.type = type;
  }

  @Override
  public T invoke(final T element) {
    return !type.isInstance(element) ? element : null;
  }
}
