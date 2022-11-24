package lance.func.tail;

import lance.func.Func.TryFunction;
import lance.func.Recursive;
import lance.func.Recursive.Tailrec;

public final class Average<T, N extends Number> extends Tailrec<T, Double, Average<T, N>> implements Recursive<T, Double, Average<T, N>> {
  private final Double accumulated;
  private final int counted;
  private final TryFunction<? super T, ? extends N> select;

  public Average(final TryFunction<? super T, ? extends N> select) {
    this(null, 0, select);
  }
  private Average(final Double accumulated, final int counted, final TryFunction<? super T, ? extends N> select) {
    this.accumulated = accumulated;
    this.counted = counted;
    this.select = select;
  }

  @Override
  public Return<T, Double, Average<T, N>> tryApply(final T it) throws Throwable {
    final var selected = select.tryApply(it);

    if (selected == null) return Return.with(accumulated, this);

    final var acc = counted == 0 ? selected.doubleValue() : accumulated + selected.doubleValue();
    final var cc = counted + 1;
    return Return.with(acc / cc, new Average<>(acc, cc, select));
  }
}
