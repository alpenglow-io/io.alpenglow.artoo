package oak.collect.query.aggregate;

import oak.collect.query.Queryable;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;

final class Expression<T, S> implements Aggregation<S> {
  private final Queryable<T> queryable;
  private final S seed;
  private final Predicate1<T> expression;
  private final Function2<S, T, S> reduce;

  Expression(final Queryable<T> queryable, final S seed, final Predicate1<T> expression, final Function2<S, T, S> reduce) {
    this.queryable = queryable;
    this.seed = seed;
    this.expression = expression;
    this.reduce = reduce;
  }

  @Override
  public final S get() {
    var reduced = seed;
    for (final var it : queryable) reduced = expression.apply(it) ? reduce.apply(reduced, it) : reduced;
    return reduced;
  }
}
