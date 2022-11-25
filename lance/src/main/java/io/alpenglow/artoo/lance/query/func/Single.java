package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TryPredicate;

public final class Single<T> implements TryFunction<T, T> {
  enum NoSingle {Found}

  private Object single = null;
  private final TryPredicate<? super T> where;

  public Single(final TryPredicate<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && single == null) {
      single = element;
    } else if (where.tryTest(element)) {
      single = NoSingle.Found;
    }

    //noinspection unchecked
    return NoSingle.Found.equals(single)
      ? null
      : (T) single;
  }
}
