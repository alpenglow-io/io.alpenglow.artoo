package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Average;

public interface Averageable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<Double> average(final Func.Bi<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Average<>(pair -> select.tryApply(pair.first(), pair.second()))).keepNull();
  }
}
