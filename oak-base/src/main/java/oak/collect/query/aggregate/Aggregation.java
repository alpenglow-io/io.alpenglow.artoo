package oak.collect.query.aggregate;

import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.collect.query.Maybe;
import oak.collect.query.Queryable;

import static java.util.Objects.requireNonNull;

public interface Aggregation<T> extends Maybe<T> {
  static <S, R> Aggregation<R> seed(final Queryable<S> some, final R seed, final Function2<R, S, R> reduce) {
    return new Seed<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(seed, "Seed is null"),
      requireNonNull(reduce, "Reduce is null")
    );
  }

  static <S, R> Aggregation<R> identity(final Queryable<S> some, final Function1<S, R> identity, final Function2<R, S, R> reduce) {
    return new Identity<>(
      requireNonNull(some, "Sequence is null"),
      requireNonNull(identity, "Identity is null"),
      requireNonNull(reduce, "Reduce is null")
    );
  }

  static <S> Aggregation<Long> count(final Queryable<S> some) {
    return new Count<>(some);
  }

/*
  static <S, R> Aggregation<S, R> average(final Sequence<S> sequence, final Function2<R, S, R> reduce) {
    return new Average<>(
      new FunAggregate<>(
        requireNonNull(sequence, "Sequence is null"),
        it -> it,
        requireNonNull(reduce, "Reduce is null")
      ),
      Length.maybe(sequence)
    );
  }*/
  //<R> R aggregate(final R initial, final Function2<R, ? super T, ? extends R> reduce);
}
