package lance.query.func;

import lance.func.Func.TryFunction;
import lance.func.Pred.TryBiPredicate;

@SuppressWarnings("unchecked")
public final class Take<T, R> implements TryFunction<T, R> {
  private final Taken taken = new Taken();
  private final TryBiPredicate<? super Integer, ? super T> where;

  public Take(final TryBiPredicate<? super Integer, ? super T> where) {
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
