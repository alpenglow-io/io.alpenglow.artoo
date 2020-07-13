package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.Uni;

public final class Count<T> implements Func.Uni<T, Integer> {
  private static final class Counted { private int value = 0; }

  private final Counted counted;
  private final Uni<? super T> where;

  public Count(final Uni<? super T> where) {
    assert where != null;
    this.counted = new Counted();
    this.where = where;
  }

  @Override
  public Integer tryApply(final T element) throws Throwable {
    return element != null && where.tryTest(element) ? ++counted.value : counted.value;
  }
}
