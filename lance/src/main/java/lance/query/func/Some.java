package lance.query.func;

import lance.func.Func;
import lance.func.Pred;

public final class Some<T> implements Func.MaybeFunction<T, Boolean> {
  private final Pred.MaybePredicate<? super T> where;

  public Some(final Pred.MaybePredicate<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public final Boolean tryApply(final T element) throws Throwable {
    return element != null && where.tryTest(element);
  }
}
