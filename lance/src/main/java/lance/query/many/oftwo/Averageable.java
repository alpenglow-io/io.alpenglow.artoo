package lance.query.many.oftwo;

import lance.func.Func;
import lance.func.tail.Average;
import lance.query.One;
import lance.Queryable;

public interface Averageable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<Double> average(final Func.TryBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(new Average<>(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }
}
