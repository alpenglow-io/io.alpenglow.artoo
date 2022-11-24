package lance.query.func;

import lance.func.Cons;
import lance.func.Func;
import lance.func.Pred;

public final class WhenWhere<T> implements Func.TryFunction<T, T> {
  private final Pred.TryPredicate<? super T> where;
  private final Cons.TryConsumer<? super T> cons;

  public WhenWhere(final Pred.TryPredicate<? super T> where, final Cons.TryConsumer<? super T> cons) {
    this.where = where;
    this.cons = cons;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) cons.tryAccept(element);
    return element;
  }
}
