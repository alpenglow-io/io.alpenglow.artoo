package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Sum;
import io.artoo.lance.tuple.Pair;

public interface Summable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> sum(final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Sum<Pair<A, B>, N, N>(pair -> select.tryApply(pair.first(), pair.second()))).keepNull();
  }
}

