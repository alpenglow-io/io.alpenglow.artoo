package oak.quill.query;

import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;
import oak.quill.Structable;
import oak.quill.single.Single;
import oak.type.Numeric;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static oak.func.fun.Function1.identity;
import static oak.func.pre.Predicate1.tautology;
import static oak.type.Nullability.nonNullable;
import static oak.type.Nullability.nonNullableState;
import static oak.type.Numeric.asDouble;

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

  default Single<Long> count(final Predicate1<? super T> filter) {
    return new Count<>(this, nonNullable(filter, "filter"));
  }

  default Single<Long> count() {
    return count(tautology());
  }

  default <R, C extends Comparable<R>, V extends C> Single<V> max(final Function1<? super T, ? extends V> selector) {
    return new SelectorMinMax<>(this, nonNullable(selector, "selector"), 1);
  }

  default Single<T> max() {
    return new MinMax<>(this, 1);
  }

  default <R, C extends Comparable<R>, V extends C> Single<V> min(final Function1<? super T, ? extends V> selector) {
    return new SelectorMinMax<>(this, nonNullable(selector, "selector"), -1);
  }

  default Single<T> min() {
    return new MinMax<>(this, -1);
  }

  default <N extends Number> Single<N> sum(final Function1<? super T, ? extends N> selector) {
    return new SelectorSum<>(this, nonNullable(selector, "selector"));
  }

  default Single<T> sum() {
    return new Sum<>(this);
  }

  default <N extends Number> Single<Double> average(final Function1<? super T, ? extends N> selector) {
    return new Average<>(this, nonNullable(selector, "selector"), asDouble());
  }

  default Single<Double> average() {
    return new Average<>(this, identity(), asDouble());
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

final class SelectorMinMax<T, R, C extends Comparable<R>, V extends C> implements Single<V> {
  private final Structable<T> structable;
  private final Function1<? super T, ? extends V> map;
  private final int operation;

  @Contract(pure = true)
  SelectorMinMax(
    final Structable<T> structable,
    final Function1<? super T, ? extends V> map,
    final int operation
  ) {
    this.structable = structable;
    this.map = map;
    this.operation = operation;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final V get() {
    V result = null;
    for (final var value : structable) {
      if (nonNull(value)) {
        final var mapped = map.apply(value);
        if (isNull(result) || nonNull(mapped) && mapped.compareTo((R) result) == operation)
          result = mapped;
      }
    }
    return nonNullableState(result, operation == 1 ? "Max" : "Min");
  }
}

final class MinMax<T> implements Single<T> {
  private final Structable<T> structable;
  private final int operation;

  @Contract(pure = true)
  MinMax(final Structable<T> structable, final int operation) {
    this.structable = structable;
    this.operation = operation;
  }

  @Contract(pure = true)
  @Override
  @SuppressWarnings("unchecked")
  public final T get() {
    T result = null;
    for (final var value : structable) {
      if (nonNull(value) && value instanceof Comparable) {
        final var mapped = (Comparable<T>) value;
        if (isNull(result) || mapped.compareTo(result) == operation) {
          result = value;
        }
      }
    }
    return nonNullableState(result, operation == 1 ? "Max" : "Min", "%s must have at least one Comparable implementation item.");
  }
}

final class SelectorSum<T, N extends Number> implements Single<N> {
  private final Structable<T> structable;
  private final Function1<? super T, ? extends N> map;

  @Contract(pure = true)
  SelectorSum(final Structable<T> structable, final Function1<? super T, ? extends N> map) {
    this.structable = structable;
    this.map = map;
  }

  @Override
  public final N get() {
    N result = null;
    for (final var value : structable) {
      final var mapped = map.apply(value);
      if (isNull(result)) {
        result = mapped;
      } else if (nonNull(mapped)) {
        result = Numeric.sum(result, mapped);
      }
    }
    return nonNullableState(result, "Sum", "%s must have at least one non-null number.");
  }
}

final class Sum<T> implements Single<T> {
  private final Structable<T> structable;

  @Contract(pure = true)
  Sum(final Structable<T> structable) {this.structable = structable;}

  @SuppressWarnings("unchecked")
  @Nullable
  @Contract(pure = true)
  @Override
  public final T get() {
    T result = null;
    for (final var value : structable) {
      if (value instanceof Number) {
        if (result != null && result.getClass().equals(value.getClass())) {
          result = (T) Numeric.sum((Number) result, (Number) value);
        } else if (result == null) {
          result = value;
        }
      }
    }
    return nonNullableState(result, "Sum", "%s must have at least one non-null number.");
  }
}

final class Count<T> implements Single<Long> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Count(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Long get() {
    long count = 0;
    for (final var value : structable) {
      if (filter.test(value)) count++;
    }
    return count;
  }
}
