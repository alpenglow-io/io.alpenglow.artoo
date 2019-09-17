package oak.quill.query;

import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;
import oak.quill.Structable;
import oak.quill.single.Single;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import static oak.func.fun.Function1.identity;
import static oak.func.pre.Predicate1.tautology;
import static oak.type.Nullability.nonNullable;

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

  default <R> Single<R> max(final Function1<? super T, ? extends R> map) {
    return aggregate(tautology(), map, null, (max, next) -> max == null || max.hashCode() < next.hashCode() ? next : max);
  }

  default Single<T> max() {
    return max(identity());
  }

  default <R> Single<R> min(final Function1<? super T, ? extends R> map) {
    return aggregate(tautology(), map, null, (min, next) -> min == null || min.hashCode() > next.hashCode() ? next : min);
  }

  default Single<T> min() {
    return min(identity());
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
