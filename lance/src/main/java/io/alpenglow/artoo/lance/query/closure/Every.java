package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.query.Unit;

public final class Every<T> implements Closure<T, Boolean> {
  private final TryPredicate1<? super T> where;
  private boolean allOfThem;
  public Every(final TryPredicate1<? super T> where) {
    assert where != null;
    this.where = where;
    this.allOfThem = true;
  }

  @Override
  public Unit<Boolean> invoke(Unit<T> element) throws Throwable {
    return Unit.value(allOfThem &= where.invoke(element.value()));
  }
}
