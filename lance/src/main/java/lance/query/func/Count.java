package lance.query.func;

import lance.func.Func;
import lance.func.Pred;

public final class Count<T> implements Func.MaybeFunction<T, Integer> {
  private static final class Counted { private int value = 0; }

  private final Counted counted;
  private final Pred.MaybePredicate<? super T> where;

  public Count(final Pred.MaybePredicate<? super T> where) {
    assert where != null;
    this.counted = new Counted();
    this.where = where;
  }

  @Override
  public Integer tryApply(final T element) throws Throwable {
    return element != null && where.tryTest(element) ? ++counted.value : counted.value;
  }
}
