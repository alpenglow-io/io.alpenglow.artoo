package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Partitionable<T> extends Projectable<T> {

  default Cursor<T> skipWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return select(new Skip<>(where));
  }

  default Cursor<T> take(final int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Cursor<T> takeWhile(final Pred.Uni<? super T> where) {
    return takeWhile((index, param) -> where.test(param));
  }

  default Cursor<T> takeWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return select(new Take<>(where));
  }
}

final class Skip<T, R> implements Func.Uni<T, R> {
  private final Skipped skipped = new Skipped();
  private final Pred.Bi<? super Integer, ? super T> where;

  Skip(final Pred.Bi<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return (R) (where.tryTest(skipped.index++, element) && ++skipped.count == skipped.index ? null : element);
  }

  private final class Skipped {
    private int index = 0;
    private int count = 0;
  }
}

final class Take<T, R> implements Func.Uni<T, R> {
  private final Taken taken = new Taken();
  private final Pred.Bi<? super Integer, ? super T> where;

  Take(final Pred.Bi<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return (R) ((taken.keep = where.tryTest(taken.index++, element) && taken.keep) ? element : null);
  }

  private final class Taken {
    private int index = 0;
    private boolean keep = true;
  }
}
