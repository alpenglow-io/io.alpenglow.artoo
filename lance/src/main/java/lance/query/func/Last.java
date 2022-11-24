package lance.query.func;

import lance.func.Func;
import lance.func.Pred;

public final class Last<T> implements Func.MaybeFunction<T, T> {
  private final Pred.MaybePredicate<? super T> where;
  private final Found found;

  public Last(final Pred.MaybePredicate<? super T> where) {
    assert where != null;
    this.where = where;
    this.found = new Found();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) {
      found.value = element;
    }
    return found.value;
  }

  private final class Found {
    private T value;
  }
}
