package lance.query.func;

import lance.func.Func;
import lance.func.Pred;

public final class Where<T> implements Func.TryFunction<T, T> {
  private final class Index {
    private int value = 0;
  }

  private final Pred.TryBiPredicate<? super Integer, ? super T> where;
  private final Index index;

  public Where(final Pred.TryBiPredicate<? super Integer, ? super T> where) {
    assert where != null;
    this.where = where;
    this.index = new Index();
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return where.tryTest(index.value++, element) ? element : null;
  }
}
