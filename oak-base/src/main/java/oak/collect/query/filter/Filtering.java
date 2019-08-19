package oak.collect.query.filter;

import oak.func.pre.Predicate1;
import oak.collect.query.Maybe;
import oak.collect.query.Queryable;

import static java.util.Objects.requireNonNull;

public interface Filtering<T> extends Iterable<T> {
  static <S> Queryable<S> where(final Queryable<S> some, final Predicate1<S> filter) {
    return new WhereMany<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(filter, "Filter is null")
    );
  }

  static <S, C> Queryable<C> ofType(final Queryable<S> some, final Class<C> type) {
    return new OfType<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(type, "Type is null")
    );
  }

  static <S> Maybe<S> where(final Maybe<S> maybe, final Predicate1<S> filter) {
    return new WhereJust<>(
      requireNonNull(maybe, "Maybe is null"),
      requireNonNull(filter, "Filter is null")
    );
  }
}
