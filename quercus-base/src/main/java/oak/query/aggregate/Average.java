package oak.query.aggregate;

import io.ibex.collect.prop.Length;
import oak.query.Maybe;

import java.util.Iterator;

final class Average implements Aggregation<Double> {
  private final Maybe<Double> aggregation;
  private final Length length;

  Average(final Maybe<Double> aggregation, final Length length) {
    this.aggregation = aggregation;
    this.length = length;
  }

  @Override
  public final Iterator<Double> iterator() {
    return aggregation.select(it -> it / (double) length.get()).iterator();
  }
}
