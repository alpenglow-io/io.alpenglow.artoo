package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.next.Cursor;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Averageable<T> extends Projectable<T> {
  default <N extends Number> Cursor<Double> average(final Func.Uni<? super T, ? extends N> select) {
    return select(new Average<>(nonNullable(select, "select")));
  }

  default Cursor<Double> average() {
    return average(it -> it instanceof Number n ? n.doubleValue() : null);
  }
}

final class Average<T, N extends Number> implements Func.Uni<T, Double> {
  private final Averaged averaged;
  private final Func.Uni<? super T, ? extends N> select;

  Average(final Func.Uni<? super T, ? extends N> select) {
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



