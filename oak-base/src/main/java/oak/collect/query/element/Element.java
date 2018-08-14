package oak.collect.query.element;

import oak.collect.query.Functor;
import oak.collect.query.Maybe;
import oak.collect.query.Queryable;
import oak.collect.query.aggregate.Aggregation;

import static java.util.Objects.requireNonNull;

public interface Element<T, M extends Iterable<T>> extends Functor<T, M> {
  static <S> Maybe<S> at(final Queryable<S> some, final long index) {
    return new At<>(
      requireNonNull(some, "Some is null"),
      index
    );
  }

  static <S> Queryable<S> first(final Queryable<S> some) {
    return new First<>(new At<>(requireNonNull(some, "Some is null"), 0));
  }

  static <S> Maybe<S> single(final Queryable<S> some) {
    return new Single<>(requireNonNull(some, "Some is null"));
  }

  static <S> Queryable<S> last(final Queryable<S> some) {
    requireNonNull(some, "Some is null");
    return new Last<>(
      Aggregation.count(some),
      it -> new At<>(some, it - 1)
    );
  }
}
