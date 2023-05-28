package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;

record Avg(double folded, int count) {
  static final Avg Zero = new Avg(0.0, 0);
  <NUMBER extends Number> Avg fold(NUMBER element) {
    return new Avg(folded + element.doubleValue(), count + 1);
  }
}
public interface Averageable<ELEMENT> extends Queryable<ELEMENT> {
  default <NUMBER extends Number> One<Double> average(final TryFunction1<? super ELEMENT, ? extends NUMBER> select) {
    return () -> cursor()
      .map(select)
      .fold(Avg.Zero, Avg::fold)
      .map(avg -> avg.count() > 0 ? avg.folded() / avg.count() : avg.folded());
  }

  default One<Double> average() {
    return average(it -> it instanceof Number number ? number : null);
  }
}



