package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public final class Count<T> implements Func.Uni<T, Integer> {
  private static final class Counted { private int value = 0; }

  private final Count.Counted counted;
  private final Pred.Uni<? super T> where;

  public Count(final Pred.Uni<? super T> where) {
    assert where != null;
    this.counted = new Count.Counted();
    this.where = where;
  }

  @Override
  public Integer tryApply(final T t) throws Throwable {
    return where.tryTest(t) ? ++counted.value : counted.value;
  }
}
