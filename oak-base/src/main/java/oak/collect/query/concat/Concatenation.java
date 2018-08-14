package oak.collect.query.concat;

import oak.collect.query.Functor;
import oak.collect.query.Queryable;

import static java.util.Objects.requireNonNull;

public interface Concatenation<T, M extends Iterable<T>> extends Functor<T, M> {
  @SafeVarargs
  static <S> Queryable<S> concat(Queryable<S> some, final S... values) {
    return new Concat<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(values, "Values is null")
    );
  }

  @SafeVarargs
  static <S> Queryable<S> merge(Queryable<S> source, final Queryable<S>... lasts) {
    return new Merge<>(
      requireNonNull(source, "Source is null"),
      requireNonNull(lasts, "Lasts is null")
    );
  }
}
