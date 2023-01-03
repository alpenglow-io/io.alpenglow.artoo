package re.artoo.lance.query.closure;

import re.artoo.lance.query.Closure;

public final class At<T> implements Closure<T, T> {
  private final int at;
  public int pointed;

  public At(final int at) {
    this.at = at;
    this.pointed = 0;
  }

  @Override
  public T invoke(T element) throws Throwable {
    return pointed++ == at ? element : null;
  }
}
