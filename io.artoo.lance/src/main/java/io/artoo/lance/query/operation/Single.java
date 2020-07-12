package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.Uni;

public final class Single<T> implements Func.Uni<T, T> {
  private final Uni<? super T> where;
  private final Singled singled;

  public Single(final Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.singled = new Singled();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && singled.value == null) {
      return (singled.value = element);
    } else {
      return null;
    }
  }

  private final class Singled {
    private T value;
  }
}
