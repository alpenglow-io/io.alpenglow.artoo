package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public final class Average<T, N extends Number> implements Func.Uni<T, Double> {
  private final Averaged averaged;
  private final Func.Uni<? super T, ? extends N> select;

  public Average(final Func.Uni<? super T, ? extends N> select) {
    assert select != null;
    this.averaged = new Averaged();
    this.select = select;
  }

  @Override
  public final Double tryApply(final T it) throws Throwable {
    final var selected = select.tryApply(it);
    if (selected != null) {
      averaged.value = averaged.count == 0 ? selected.doubleValue() : averaged.value + selected.doubleValue();
      averaged.count++;
    }
    return switch (averaged.count) {
      case 0 -> averaged.value;
      default -> averaged.value / averaged.count;
    };
  }

  private final class Averaged {
    private Double value;
    private int count = 0;
  }
}
