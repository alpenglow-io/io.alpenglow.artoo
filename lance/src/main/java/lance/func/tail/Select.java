package lance.func.tail;

import lance.func.Func;
import lance.func.Recursive.Tailrec;

public final class Select<T, R> extends Tailrec<T, R, Select<T, R>> {
  private final Func.TryBiFunction<? super Integer, ? super T, ? extends R> select;

  private final int index;

  private Select(final int index, final Func.TryBiFunction<? super Integer, ? super T, ? extends R> select) {
    assert select != null;
    this.index = index;
    this.select = select;
  }

  @Override
  public Return<T, R, Select<T, R>> tryApply(final T t) throws Throwable {
    return Return.with(select.tryApply(index, t), new Select<>(index + 1, select));
  }

  public static <T, R> Select<T, R> with(Func.TryFunction<? super T, ? extends R> select) {
    return new Select<>(0, (index, it) -> select.tryApply(it));
  }

  public static <T, R> Select<T, R> with(Func.TryBiFunction<? super Integer, ? super T, ? extends R> select) {
    return new Select<>(0, select);
  }
}
