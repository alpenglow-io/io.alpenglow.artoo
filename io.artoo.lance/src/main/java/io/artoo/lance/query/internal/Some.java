package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

import java.util.ArrayList;
import java.util.Collection;

public final class Some<T> implements Func.Uni<T, Boolean> {
  private final Pred.Uni<? super T> where;
  private final Collection<T> collected;

  public Some(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.collected = new ArrayList<>();
  }

  @Override
  public final Boolean tryApply(final T element) throws Throwable {
    if (element != null && where.tryTest(element)) {
      collected.add(element);
      return true;
    } else {
      return false;
    }
  }
}
