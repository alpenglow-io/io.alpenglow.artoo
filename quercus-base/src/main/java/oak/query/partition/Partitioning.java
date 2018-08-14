package oak.query.partition;

import oak.func.pre.Predicate1;
import oak.query.Functor;
import oak.query.Queryable;

import static java.util.Objects.requireNonNull;

public interface Partitioning<T, M extends Iterable<T>> extends Functor<T, M> {
  static <S> Queryable<S> skip(final Queryable<S> source, final int until) {
    return new Skip<>(
      requireNonNull(source, "Source is null"),
      until
    );
  }

  static <S> Queryable<S> take(final Queryable<S> source, final int until) {
    return new Take<>(
      requireNonNull(source, "Source is null"),
      until
    );
  }

  static <S> Queryable<S> skipWhile(final Queryable<S> source, final Predicate1<S> expression) {
    return new SkipWhile<>(
      requireNonNull(source, "Source is null"),
      requireNonNull(expression, "Expression is null")
    );
  }

  static <S> Queryable<S> takeWhile(final Queryable<S> source, final Predicate1<S> expression) {
    return new TakeWhile<>(
      requireNonNull(source, "Source is null"),
      requireNonNull(expression, "Expression is null")
    );
  }
}
