package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

public final class Last<T> implements Closure<T, T> {
  private final TryPredicate1<? super T> where;
  private final Found found;

  public Last(final TryPredicate1<? super T> where) {
    assert where != null;
    this.where = where;
    this.found = new Found();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    if (where.invoke(element)) {
      found.value = element;
    }
    return found.value;
  }

  private final class Found {
    private T value;
  }
}
