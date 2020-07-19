package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

import java.util.ArrayList;
import java.util.Collection;

public final class Distinct<T> implements Func.Uni<T, T> {
  private final Pred.Uni<? super T> where;
  private final Collection<T> collected;

  public Distinct(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.collected = new ArrayList<>();
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && !collected.contains(element)) {
      collected.add(element);
      return element;
    } else if (where.tryTest(element) && collected.contains(element)) {
      return null;
    } else {
      return element;
    }
  }
}
