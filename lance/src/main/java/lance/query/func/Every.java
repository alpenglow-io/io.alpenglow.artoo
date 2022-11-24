package lance.query.func;

import lance.func.Func;
import lance.func.Pred.TryPredicate;

public final class Every<T> implements Func.TryFunction<T, Boolean> {
  private final TryPredicate<? super T> where;
  private final AllOfThem allOfThem;

  public Every(final TryPredicate<? super T> where) {
    assert where != null;
    this.where = where;
    this.allOfThem = new AllOfThem();
  }

  @Override
  public Boolean tryApply(final T element) throws Throwable {
    return (allOfThem.value &= where.tryTest(element));
  }

  private static final class AllOfThem {
    private boolean value = true;
  }
}
