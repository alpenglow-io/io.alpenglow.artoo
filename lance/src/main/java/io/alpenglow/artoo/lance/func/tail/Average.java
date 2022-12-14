package io.alpenglow.artoo.lance.func.tail;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.Recursive;
import io.alpenglow.artoo.lance.func.Recursive.Tailrec;

public final class Average<T, N extends Number> extends Tailrec<T, Double, Average<T, N>> implements Recursive<T, Double, Average<T, N>> {
  private final Double accumulated;
  private final int counted;
  private final TryFunction1<? super T, ? extends N> select;

  public Average(final TryFunction1<? super T, ? extends N> select) {
    this(null, 0, select);
  }
  private Average(final Double accumulated, final int counted, final TryFunction1<? super T, ? extends N> select) {
    this.accumulated = accumulated;
    this.counted = counted;
    this.select = select;
  }

  @Override
  public Return<T, Double, Average<T, N>> invoke(final T it) throws Throwable {
    final var selected = select.invoke(it);

    if (selected == null) return Return.with(accumulated, this);

    final var acc = counted == 0 ? selected.doubleValue() : accumulated + selected.doubleValue();
    final var cc = counted + 1;
    return Return.with(acc / cc, new Average<>(acc, cc, select));
  }
}
