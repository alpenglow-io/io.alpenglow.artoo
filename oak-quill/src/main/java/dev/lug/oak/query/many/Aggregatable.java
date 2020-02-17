package dev.lug.oak.query.many;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.func.$2.Fun;
import dev.lug.oak.func.Pre;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.query.one.One;
import dev.lug.oak.type.Nullability;
import dev.lug.oak.type.Numeric;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.func.Fun.identity;
import static dev.lug.oak.func.Pre.tautology;
import static dev.lug.oak.type.Nullability.nonNullableState;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@SuppressWarnings("unused")
public interface Aggregatable<T> extends Queryable<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  private <A, R> One<A> aggregate(final Pre<? super T> filter, final dev.lug.oak.func.Fun map, final A seed, final Fun<? super A, ? super R, ? extends A> reduce) {
    return new Aggregate<>(
      this,
      Nullability.nonNullable(filter, "filter"),
      Nullability.nonNullable(map, "map"),
      seed,
      Nullability.nonNullable(reduce, "reduce")
    );
  }

  default <A, R> One<A> aggregate(final dev.lug.oak.func.Fun map, final A seed, final Fun<? super A, ? super R, ? extends A> reduce) {
    return new Aggregate<>(
      this,
      tautology(),
      Nullability.nonNullable(map, "map"),
      seed,
      Nullability.nonNullable(reduce, "reduce")
    );
  }

  default <A> One<A> aggregate(final A seed, final Fun<? super A, ? super T, ? extends A> reduce) {
    return new Aggregate<>(
      this,
      tautology(),
      identity(),
      seed,
      Nullability.nonNullable(reduce, "reduce")
    );
  }

  default One<T> aggregate(final Fun<? super T, ? super T, ? extends T> reduce) {
    return new NoSeed<>(this, Nullability.nonNullable(reduce, "reduce"));
  }

  default One<Long> count(final Pre<? super T> filter) {
    return new Count<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default One<Long> count() {
    return count(tautology());
  }

  default <R, C extends Comparable<R>, V extends C> One<V> max(final dev.lug.oak.func.Fun selector) {
    return new SelectorMinMax<>(this, Nullability.nonNullable(selector, "selector"), 1);
  }

  default One<T> max() {
    return new MinMax<>(this, 1);
  }

  default <R, C extends Comparable<R>, V extends C> One<V> min(final dev.lug.oak.func.Fun selector) {
    return new SelectorMinMax<>(this, Nullability.nonNullable(selector, "selector"), -1);
  }

  default One<T> min() {
    return new MinMax<>(this, -1);
  }

  default <N extends Number> One<N> sum(final dev.lug.oak.func.Fun selector) {
    return new SelectorSum<>(this, Nullability.nonNullable(selector, "selector"));
  }

  default One<T> sum() {
    return new Sum<>(this);
  }

  default <N extends Number> One<Double> average(final dev.lug.oak.func.Fun selector) {
    return new Average<>(this, Nullability.nonNullable(selector, "selector"), Numeric.asDouble());
  }

  default One<Double> average() {
    return new Average<>(this, identity(), Numeric.asDouble());
  }
}

final class Aggregate<T, A, R> implements One<A> {
  private final Queryable<T> queryable;
  private final Pre<? super T> filter;
  private final dev.lug.oak.func.Fun map;
  private final A seed;
  private final Fun<? super A, ? super R, ? extends A> reduce;

  @Contract(pure = true)
  Aggregate(
    final Queryable<T> queryable,
    final Pre<? super T> filter,
    final dev.lug.oak.func.Fun map,
    final A seed,
    final Fun<? super A, ? super R, ? extends A> reduce
  ) {
    this.queryable = queryable;
    this.filter = filter;
    this.map = map;
    this.seed = seed;
    this.reduce = reduce;
  }

  @Override
  public final Iterator<A> iterator() {
    var returned = seed;
    for (final var value : queryable) {
      if (filter.test(value)) {
        final var apply = map.apply(value);
        returned = reduce.apply(returned, apply);
      }
    }
    return Cursor.once(returned);
  }
}

final class NoSeed<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Fun<? super T, ? super T, ? extends T> reduce;

  @Contract(pure = true)
  NoSeed(final Queryable<T> queryable, final Fun<? super T, ? super T, ? extends T> reduce) {
    this.queryable = queryable;
    this.reduce = reduce;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T returned = null;
    for (final var value : queryable) {
      returned = returned == null ? value : reduce.apply(returned, value);
    }
    return Cursor.once(returned);
  }
}

final class Average<T, V> implements One<Double> {
  private final Queryable<T> queryable;
  private final dev.lug.oak.func.Fun map;
  private final dev.lug.oak.func.Fun asDouble;

  @Contract(pure = true)
  Average(
    final Queryable<T> queryable,
    final dev.lug.oak.func.Fun map,
    final dev.lug.oak.func.Fun asDouble
  ) {
    this.queryable = queryable;
    this.map = map;
    this.asDouble = asDouble;
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public final Iterator<Double> iterator() {
    var total = 0.0;
    var count = 0;
    for (final var next : queryable) {
      if (nonNull(next)) {
        final var value = map.andThen(asDouble).apply(next);
        if (nonNull(value)) {
          total += value;
          count++;
        }
      }
    }
    return count == 0 ? Cursor.none() : Cursor.once(total / count);
  }
}

final class SelectorMinMax<T, R, C extends Comparable<R>, V extends C> implements One<V> {
  private final Queryable<T> queryable;
  private final dev.lug.oak.func.Fun map;
  private final int operation;

  @Contract(pure = true)
  SelectorMinMax(
    final Queryable<T> queryable,
    final dev.lug.oak.func.Fun map,
    final int operation
  ) {
    this.queryable = queryable;
    this.map = map;
    this.operation = operation;
  }

  @NotNull
  @Override
  @SuppressWarnings("unchecked")
  public final Iterator<V> iterator() {
    V result = null;
    for (final var value : queryable) {
      if (nonNull(value)) {
        final var mapped = map.apply(value);
        if (isNull(result) || nonNull(mapped) && mapped.compareTo((R) result) == operation)
          result = mapped;
      }
    }
    return Cursor.once(nonNullableState(result, operation == 1 ? "Max" : "Min"));
  }
}

final class MinMax<T> implements One<T> {
  private final Queryable<T> queryable;
  private final int operation;

  @Contract(pure = true)
  MinMax(final Queryable<T> queryable, final int operation) {
    this.queryable = queryable;
    this.operation = operation;
  }

  @Contract(pure = true)
  @Override
  @SuppressWarnings("unchecked")
  public final Iterator<T> iterator() {
    T result = null;
    for (final var value : queryable) {
      if (nonNull(value) && value instanceof Comparable) {
        final var mapped = (Comparable<T>) value;
        if (isNull(result) || mapped.compareTo(result) == operation) {
          result = value;
        }
      }
    }
    return Cursor.once(nonNullableState(result, operation == 1 ? "Max" : "Min", "%s must have at least one Comparable implementation item."));
  }
}

final class SelectorSum<T, N extends Number> implements One<N> {
  private final Queryable<T> queryable;
  private final dev.lug.oak.func.Fun map;

  @Contract(pure = true)
  SelectorSum(final Queryable<T> queryable, final dev.lug.oak.func.Fun map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<N> iterator() {
    N result = null;
    for (final var value : queryable) {
      final var mapped = map.apply(value);
      if (isNull(result)) {
        result = mapped;
      } else if (nonNull(mapped)) {
        result = Numeric.sum(result, mapped);
      }
    }
    return Cursor.once(nonNullableState(result, "Sum", "%s must have at least one non-null number."));
  }
}

final class Sum<T> implements One<T> {
  private final Queryable<T> queryable;

  @Contract(pure = true)
  Sum(final Queryable<T> queryable) {this.queryable = queryable;}

  @SuppressWarnings("unchecked")
  @Contract(pure = true)
  @Override
  public final Iterator<T> iterator() {
    T result = null;
    for (final var value : queryable) {
      if (value instanceof Number) {
        if (result != null && result.getClass().equals(value.getClass())) {
          result = (T) Numeric.sum((Number) result, (Number) value);
        } else if (result == null) {
          result = value;
        }
      }
    }
    return Cursor.once(nonNullableState(result, "Sum", "%s must have at least one non-null number."));
  }
}

final class Count<T> implements One<Long> {
  private final Queryable<T> queryable;
  private final Pre<? super T> filter;

  @Contract(pure = true)
  Count(final Queryable<T> queryable, final Pre<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<Long> iterator() {
    long count = 0;
    for (final var value : queryable) {
      if (filter.test(value)) count++;
    }
    return Cursor.once(count);
  }
}
