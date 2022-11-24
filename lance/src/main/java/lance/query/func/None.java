package lance.query.func;

import lance.func.Func;
import lance.func.Pred.TryPredicate;

public final class None<T> implements Func.TryFunction<T, Boolean> {
  private final TryPredicate<? super T> where;
  private final NoneOfThem noneOfThem;

  public None(final TryPredicate<? super T> where) {
    assert where != null;
    this.where = where;
    this.noneOfThem = new NoneOfThem();
  }

  @Override
  public Boolean tryApply(final T element) throws Throwable {
    return (noneOfThem.value &= !where.tryTest(element));
  }

  private static final class NoneOfThem {
    private boolean value = true;
  }
}
