package oak.quill.query;

import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;
import oak.func.pre.Predicate2;
import oak.quill.Structable;
import oak.quill.single.Nullable;
import oak.quill.single.Single;
import oak.quill.tuple.Tuple;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static oak.func.fun.Function1.identity;
import static oak.func.pre.Predicate1.tautology;
import static oak.type.Nullability.nonNullable;

enum NumberType {
  NaN,
  Double,
  Integer,
  Long,
  BigInteger,
  BigDecimal;

  static <N> NumberType from(@NotNull final Class<N> number) {
    try {
      return NumberType.valueOf(number.getSimpleName());
    } catch (Exception e) {
      return NumberType.NaN;
    }
  }
}

public interface Aggregatable<T> extends Structable<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  private <A, R> Single<A> aggregate(final Predicate1<? super T> filter, final Function1<? super T, ? extends R> map, final A seed, final Function2<? super A, ? super R, ? extends A> reduce) {
    return new Aggregate<>(
      this,
      nonNullable(filter, "filter"),
      nonNullable(map, "map"),
      seed,
      nonNullable(reduce, "reduce")
    );
  }

  default <A, R> Single<A> aggregate(final Function1<? super T, ? extends R> map, final A seed, final Function2<? super A, ? super R, ? extends A> reduce) {
    return new Aggregate<>(
      this,
      tautology(),
      nonNullable(map, "map"),
      seed,
      nonNullable(reduce, "reduce")
    );
  }

  default <A> Single<A> aggregate(final A seed, final Function2<? super A, ? super T, ? extends A> reduce) {
    return new Aggregate<>(
      this,
      tautology(),
      identity(),
      seed,
      nonNullable(reduce, "reduce")
    );
  }

  default Single<T> aggregate(final Function2<? super T, ? super T, ? extends T> reduce) {
    return new NoSeed<>(this, nonNullable(reduce, "reduce"));
  }

  default Single<Integer> count(final Predicate1<? super T> filter) {
    return aggregate(filter, identity(), 0, (acc, next) -> acc + 1);
  }

  default Single<Integer> count() {
    return count(tautology());
  }

  default <N extends Number> Nullable<N> max(final Function1<? super T, ? extends N> map) {
    return new MinMax<>(this, map, (next, acc) -> next.doubleValue() > acc.doubleValue());
  }

  default Nullable<Long> max() {
    return max(t -> Long.valueOf(t.hashCode()));
  }

  default <N extends Number> Nullable<N> min(final Function1<? super T, ? extends N> map) {
    return new MinMax<>(this, map, (next, acc) -> next.doubleValue() < acc.doubleValue());
  }

  default Nullable<Long> min() {
    return min(t -> Long.valueOf(t.hashCode()));
  }

  default Single<Integer> sum(final Function1<? super T, Integer> map) {
    return aggregate(tautology(), map, null, (acc, next) -> acc == null ? next : acc + next);
  }

  default Single<Long> sumAsLong(final Function1<? super T, Long> map) {
    return aggregate(tautology(), map, null, (acc, next) -> acc == null ? next : acc + next);
  }

  default Single<Double> sumAsDouble(final Function1<? super T, Double> map) {
    return aggregate(tautology(), map, null, (acc, next) -> acc == null ? next : acc + next);
  }

  default Single<BigInteger> sumAsBigInteger(final Function1<? super T, BigInteger> map) {
    return aggregate(tautology(), map, null, (acc, next) -> acc == null ? next : acc.add(next));
  }

  default Single<BigDecimal> sumAsBigDecimal(final Function1<? super T, BigDecimal> map) {
    return aggregate(tautology(), map, null, (acc, next) -> acc == null ? next : acc.add(next));
  }

  default <N extends Number> Single<Double> average(final Function1<? super T, ? extends N> map) {
    return new Average<>(this, nonNullable(map, "map"), asDouble());
  }

  default Single<Double> average() {
    return new Average<>(this, identity(), asDouble());
  }

  @NotNull
  @Contract(pure = true)
  private <V> Function1<? super V, Double> asDouble() {
    return it -> isNull(it)
      ? null
      : switch (NumberType.from(it.getClass())) {
      case Double -> (Double) it;
      case Integer -> ((Integer) it).doubleValue();
      case Long -> ((Long) it).doubleValue();
      case BigInteger -> ((BigInteger) it).doubleValue();
      case BigDecimal -> ((BigDecimal) it).doubleValue();
      case NaN -> null;
    };
  }
}

final class Aggregate<T, A, R> implements Single<A> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;
  private final Function1<? super T, ? extends R> map;
  private final A seed;
  private final Function2<? super A, ? super R, ? extends A> reduce;

  @Contract(pure = true)
  Aggregate(
    final Structable<T> structable,
    final Predicate1<? super T> filter,
    final Function1<? super T, ? extends R> map,
    final A seed,
    final Function2<? super A, ? super R, ? extends A> reduce
  ) {
    this.structable = structable;
    this.filter = filter;
    this.map = map;
    this.seed = seed;
    this.reduce = reduce;
  }

  @Override
  public final A get() {
    var returned = seed;
    for (final var value : structable) {
      if (filter.test(value)) {
        final var apply = map.apply(value);
        returned = reduce.apply(returned, apply);
      }
    }
    return returned;
  }
}

final class NoSeed<T> implements Single<T> {
  private final Structable<T> structable;
  private final Function2<? super T, ? super T, ? extends T> reduce;

  @Contract(pure = true)
  NoSeed(final Structable<T> structable, final Function2<? super T, ? super T, ? extends T> reduce) {
    this.structable = structable;
    this.reduce = reduce;
  }

  @Override
  public final T get() {
    T returned = null;
    for (final var value : structable) {
      returned = returned == null ? value : reduce.apply(returned, value);
    }
    return returned;
  }
}

final class Average<T, V> implements Single<Double> {
  private final Structable<T> structable;
  private final Function1<? super T, ? extends V> map;
  private final Function1<? super V, Double> asDouble;

  @Contract(pure = true)
  Average(
    final Structable<T> structable,
    final Function1<? super T, ? extends V> map,
    final Function1<? super V, Double> asDouble
  ) {
    this.structable = structable;
    this.map = map;
    this.asDouble = asDouble;
  }

  @NotNull
  @Override
  public final Double get() {
    var total = 0.0;
    var count = 0;
    for (final var next : structable) {
      if (nonNull(next)) {
        final var value = map.andThen(asDouble).apply(next);
        if (nonNull(value)) {
          total += value;
          count++;
        }
      }
    }
    if (count == 0)
      throw new IllegalStateException("Query can't be satisfied, Queryable is empty.");
    return total / count;
  }
}

final class MinMax<T, R> implements Nullable<R> {
  private final Structable<T> structable;
  private final Function1<? super T, ? extends R> map;
  private final Predicate2<? super R, ? super R> operator;

  @Contract(pure = true)
  MinMax(
    final Structable<T> structable,
    final Function1<? super T, ? extends R> map,
    final Predicate2<? super R, ? super R> operator
  ) {
    this.structable = structable;
    this.map = map;
    this.operator = operator;
  }

  @Override
  public final R get() {
    R minMax = null;
    for (final var value : structable) {
      if (nonNull(value)) {
        final var mapped = map.apply(value);
        if (isNull(minMax) || nonNull(mapped) && operator.apply(mapped, minMax))
          minMax = mapped;
      }
    }
    return minMax;
  }
}
