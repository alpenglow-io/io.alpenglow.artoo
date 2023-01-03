package re.artoo.lance.query.closure;

import re.artoo.lance.query.Closure;

public final class OfType<T, R> implements Closure<T, R> {
  private final Class<? extends R> type;

  public OfType(final Class<? extends R> type) {
    this.type = type;
  }

  @Override
  public final R invoke(final T element) {
    return type.isInstance(element) ? type.cast(element) : null;
  }
}
