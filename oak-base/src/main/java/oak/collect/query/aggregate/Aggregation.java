package oak.collect.query.aggregate;

import oak.collect.query.Maybe;
import oak.collect.query.Queryable;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;

import static java.util.Objects.requireNonNull;

public interface Aggregation<T> extends Maybe<T> {
  static <S> Aggregation<S> accumulate(final Queryable<S> queryable, final Function2<S, S, S> reduce) {
    return new Accumulate<>(
      requireNonNull(queryable, "Queryable is null"),
      requireNonNull(reduce, "Reduce is null")
    );
  }

  static <S, R> Aggregation<R> seed(final Queryable<S> queryable, final R seed, final Function2<R, S, R> reduce) {
    return new Seed<>(
      requireNonNull(queryable, "Queryable is null"),
      requireNonNull(seed, "Seed is null"),
      requireNonNull(reduce, "Reduce is null")
    );
  }

  static <S, R> Aggregation<R> expression(final Queryable<S> queryable, final R seed, final Predicate1<S> expression, final Function2<R, S, R> reduce) {
    return new Expression<>(
      requireNonNull(queryable, "Queryable is null"),
      requireNonNull(seed, "Seed is null"),
      requireNonNull(expression, "Expression is null"),
      requireNonNull(reduce, "Reduce is null")
    );
  }

  static <S> Aggregation<Long> count(final Queryable<S> some) {
    return new Count<>(some);
  }
}
