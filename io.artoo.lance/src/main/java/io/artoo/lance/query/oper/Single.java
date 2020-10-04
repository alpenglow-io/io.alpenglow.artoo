package io.artoo.lance.query.oper;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred.Uni;

public final class Single<T> implements Func.Uni<T, Integer> {
  private static final int NOT_FOUND = -1;
  private static final int NON_SINGLE = -2;

  private final Uni<? super T> where;
  private final Singled singled;

  public Single(final Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.singled = new Singled();
  }

  @Override
  public final Integer tryApply(final T element) throws Throwable {
    ++singled.index;

    if (where.tryTest(element) && singled.singleton == NOT_FOUND) {
      singled.singleton = singled.index; // found

    } else if (where.tryTest(element) && singled.singleton >= 0) {
      singled.singleton = NON_SINGLE; // non-single
    }

    return singled.singleton;
  }

  private final class Singled {
    private int index = -1;
    private int singleton = -1;
  }
}
