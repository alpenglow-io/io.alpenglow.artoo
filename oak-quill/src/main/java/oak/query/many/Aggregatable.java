package oak.query.many;

import oak.cursor.Cursor;
import oak.func.$2.Func;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.one.One;
import oak.type.Numeric;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.lang.Integer.compare;
import static oak.func.Func.identity;
import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;
import static oak.type.Numeric.asDouble;
import static oak.type.Numeric.asNumber;
import static oak.type.Numeric.divide;
import static oak.type.Numeric.one;
import static oak.type.Numeric.zero;

public interface Aggregatable<T> extends Count<T>, Sum<T>, Average<T>, Extremum<T> {
}

interface Aggregate<T> extends Queryable<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A, R> One<A> aggregate(final A seed, final Pred<? super T> where, final oak.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    nonNullable(aggregate, "aggregate");
    return () -> {
      var returned = seed;
      for (final var value : Aggregate.this) {
        if (where.test(value)) {
          final var apply = select.apply(value);
          returned = aggregate.apply(returned, apply);
        }
      }
      return Cursor.once(returned);
    };
  }

  default <A, R> One<A> aggregate(final A seed, final oak.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(seed, tautology(), select, aggregate);
  }

  default <A, R> One<A> aggregate(final oak.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(null, tautology(), select, aggregate);
  }

  default <A> One<A> aggregate(final A seed, final Func<? super A, ? super T, ? extends A> aggregate) {
    return this.aggregate(seed, tautology(), identity(), aggregate);
  }

  default One<T> aggregate(final Func<? super T, ? super T, ? extends T> aggregate) {
    return this.aggregate(null, tautology(), identity(), aggregate);
  }
}

interface Sum<T> extends Aggregate<T> {
  default <V, N extends Number> One<N> sum(final oak.func.Func<? super T, ? extends V> select, final oak.func.Func<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return this.<N, N>aggregate(null, tautology(), value -> select.andThen(asNumber).apply(value), Numeric::sum);
  }

  default <V, N extends Number> One<N> sum(final oak.func.Func<? super T, ? extends V> select) {
    return this.<V, N>sum(select, asNumber());
  }

  default <N extends Number> One<N> sum() {
    return this.<T, N>sum(identity(), asNumber());
  }
}

interface Average<T> extends Queryable<T> {
  default <V, N extends Number> One<N> average(final oak.func.Func<? super T, ? extends V> select, final oak.func.Func<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return () -> {
      N total = null;
      N count = null;
      for (final var next : this) {
        if (next != null) {
          final var value = select.andThen(asNumber).apply(next);
          if (value != null) {
            total = Numeric.sum(total, value);
            count = Numeric.sum(count, one(value));
          }
        }
      }
      return count == null || count.equals(zero(count)) ? Cursor.none() : Cursor.once(divide(total, count));
    };
  }

  default <V, N extends Number> One<N> average(final oak.func.Func<? super T, ? extends V> select) {
    return this.<V, N>average(select, asNumber());
  }

  default One<Double> average() {
    return average(identity(), asDouble());
  }
}

interface Extremum<T> extends Aggregate<T> {
  @NotNull
  private static <T, R> oak.func.Func<? super T, Comparable<? super R>> comparing() {
    return mapped -> result -> compare(result.hashCode(), mapped.hashCode());
  }

  default <R> One<R> max(final oak.func.Func<? super T, ? extends R> select) {
    return this.<R>ext(select, comparing(), 1);
  }

  default One<T> max() {
    return this.<T>ext(identity(), comparing(), 1);
  }

  default <R> One<R> min(final oak.func.Func<? super T, ? extends R> select) {
    return this.<R>ext(select, comparing(), -1);
  }

  default One<T> min() {
    return this.<T>ext(identity(), comparing(), -1);
  }

  private <R> One<R> ext(final oak.func.Func<? super T, ? extends R> select, final oak.func.Func<? super R, Comparable<? super R>> where, final int ends) {
    //this.<R, R>aggregate(null, tautology(), select, (current, next) -> where.apply(next).compareTo(current) == ends ? next : current);
    nonNullable(select, "select");
    return () -> {
      R result = null;
      for (final var value : this) {
        if (value != null) {
          final var mapped = select.apply(value);
          if (result == null || mapped != null && where.apply(mapped).compareTo(result) == ends)
            result = mapped;
        }
      }
      return Cursor.ofNullable(result);
    };
  }
}

interface Count<T> extends Aggregate<T> {
  default One<Long> count() {
    return this.count(tautology());
  }

  default One<Long> count(final Pred<? super T> where) {
    return this.aggregate(0L, where, identity(), (count, item) -> count + 1);
  }
}
