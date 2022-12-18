package io.alpenglow.artoo.lance.func.tail;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.Recursive;
import io.alpenglow.artoo.lance.func.Recursive.Tailrec;
import io.alpenglow.artoo.lance.query.Closure;

public final class Average<T, N extends Number> implements Closure<T, Double> {
  private double accumulated;
  private int counted;
  private final TryFunction1<? super T, ? extends N> select;

  public Average(final TryFunction1<? super T, ? extends N> select) {
    this(0, 0, select);
  }
  private Average(final double accumulated, final int counted, final TryFunction1<? super T, ? extends N> select) {
    this.accumulated = accumulated;
    this.counted = counted;
    this.select = select;
  }

  @Override
  public Double invoke(final T element) throws Throwable {
    final var selected = select.invoke(element);

    if (selected == null) return accumulated;

    return (accumulated = counted == 0 ? selected.doubleValue() : accumulated + selected.doubleValue()) / counted++;
  }
}
