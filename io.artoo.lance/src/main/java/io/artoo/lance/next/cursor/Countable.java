package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Countable<T> extends Projectable<T> {
  default Cursor<Integer> count(final Pred.Uni<? super T> where) {
    return select(new Count<>(0, where)).or(0);
  }
}

final class Count<T> implements Func.Uni<T, Integer> {
  private int counter;
  private final Pred.Uni<? super T> where;

  Count(final int counter, final Pred.Uni<? super T> where) {
    this.counter = counter;
    this.where = where;
  }

  @Override
  public Integer tryApply(final T t) throws Throwable {
    return where.tryTest(t) ? ++counter : counter;
  }
}


