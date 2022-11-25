package io.alpenglow.artoo.lance.func.tail;

import io.alpenglow.artoo.lance.func.Recursive.Tailrec;
import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.func.TryFunction;

public final class Select<T, R> extends Tailrec<T, R, Select<T, R>> {
  private final TryBiFunction<? super Integer, ? super T, ? extends R> select;
  private final int index;

  private Select(final int index, final TryBiFunction<? super Integer, ? super T, ? extends R> select) {
    assert select != null;
    this.index = index;
    this.select = select;
  }

  @Override
  public Return<T, R, Select<T, R>> tryApply(final T t) throws Throwable {
    return Return.with(select.tryApply(index, t), new Select<>(index + 1, select));
  }

  public static <T, R> Select<T, R> with(TryFunction<? super T, ? extends R> select) {
    return new Select<>(0, (index, it) -> select.tryApply(it));
  }

  public static <T, R> Select<T, R> with(TryBiFunction<? super Integer, ? super T, ? extends R> select) {
    return new Select<>(0, select);
  }
}
