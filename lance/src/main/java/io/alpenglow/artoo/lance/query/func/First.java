package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TryPredicate;

public final class First<T> implements TryFunction<T, T> {
  private final TryPredicate<? super T> where;
  private final Found found;

  public First(final TryPredicate<? super T> where) {
    assert where != null;
    this.where = where;
    this.found = new Found();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && found.value == null) {
      found.value = element;
    }

    return found.value;
  }

  private final class Found {
    private T value;
  }
}
