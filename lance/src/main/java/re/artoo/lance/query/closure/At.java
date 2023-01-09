package re.artoo.lance.query.closure;

import re.artoo.lance.query.IntClosure;

public final class At<T> implements IntClosure<T, T> {
  private final int index;

  public At(final int index) {
    this.index = index;
  }

  @Override
  public T invoke(int index, T element) throws Throwable {
    return this.index == index ? element : null;
  }
}
