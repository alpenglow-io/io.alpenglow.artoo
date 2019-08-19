package oak.collect.query.aggregate;

import oak.collect.query.Maybe;
import oak.collect.query.Queryable;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;
import oak.type.AsInt;
import oak.type.AsLong;
import org.jetbrains.annotations.Contract;

import java.util.NoSuchElementException;

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

  static <S> long countAsLong(final Queryable<S> some) {
    return new Count<>(some)
      .select(it -> (long) it)
      .otherwise("Query can't be solved", NoSuchElementException::new);
  }

  static <S> long countAsLong(final Queryable<S> some, final Predicate1<S> filter) {
    return new Count<>(some, filter)
      .select(it -> (long) it)
      .otherwise("Query can't be solved", NoSuchElementException::new);
  }

  static <S> int count(final Queryable<S> some) {
    return new Count<>(some).get();
  }

  static <S> int count(final Queryable<S> some, final Predicate1<S> filter) {
    return new Count<>(some, filter).get();
  }
}

final class Count<T> implements Aggregation<Integer>, AsInt {
  private final Queryable<T> some;
  private final Predicate1<T> filter;

  Count(final Queryable<T> some) {
    this(
      some,
      value -> true
    );
  }
  Count(final Queryable<T> some, final Predicate1<T> filter) {
    this.some = some;
    this.filter = filter;
  }

  @Override
  @Contract(pure = true)
  public int get() {
    var count = 0;
    for (final var value : some) if (filter.test(value)) count++;
    return count;
  }
}
