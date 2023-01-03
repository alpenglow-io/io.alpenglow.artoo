package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Closure;

public final class Average<T, N> implements Closure<T, Double> {
  private Double accumulated;
  private int counted;
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
  public Double invoke(final T element) throws Throwable {
    return switch (select.invoke(element)) {
      case Number it when counted == 0 -> (accumulated = it.doubleValue()) / ++counted;
      case Number it when counted > 0 -> (accumulated += it.doubleValue()) / ++counted;
      case null, default -> accumulated;
    };
  }
}
