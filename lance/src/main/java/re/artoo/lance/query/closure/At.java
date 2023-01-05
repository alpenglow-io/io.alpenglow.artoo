package re.artoo.lance.query.closure;

import re.artoo.lance.query.Closure;

public final class At<T> extends Self<At<T>> implements Closure<T, T> {
  private final int index;
  private final int pointed;
  private final T element;

  public At(final int index) {
    this(index, 0, null);
  }

  private At(int index, int pointed, T element) {
    this.index = index;
    this.pointed = pointed;
    this.element = element;
  }

  @Override
  public T invoke(T element) throws Throwable {
    return write(it -> it.pointed == it.index ? next(it, element) : next(it)).element;
  }

  private At<T> next(At<T> self) {
    return next(self, null);
  }

  private At<T> next(At<T> self, T element) {
    return new At<>(self.index, self.pointed + 1, element);
  }
}
