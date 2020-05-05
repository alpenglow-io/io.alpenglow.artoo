package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.many.impl.Aggregate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;
import static java.util.function.Function.identity;

public interface Aggregatable<T extends Record> extends Countable<T>, Summable<T>, Averageable<T>, Extremumable<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A extends Record, R> One<A> aggregate(final A seed, final Predicate<? super T> where, final Function<? super T, ? extends R> select, final BiFunction<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<T, A, R>(this, it -> {}, seed, nonNullable(where, "where"), nonNullable(select, "select"), nonNullable(aggregate, "aggregate"))::iterator;
  }

  default <A extends Record, R> One<A> aggregate(final A seed, final Function<? super T, ? extends R> select, final BiFunction<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(seed, it -> true, select, aggregate);
  }

  default <A extends Record, R> One<A> aggregate(final Function<? super T, ? extends R> select, final BiFunction<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(null, it -> true, select, aggregate);
  }

  default <A extends Record> One<A> aggregate(final A seed, final BiFunction<? super A, ? super T, ? extends A> aggregate) {
    return this.aggregate(seed, it -> true, identity(), aggregate);
  }

  default One<T> aggregate(final BiFunction<? super T, ? super T, ? extends T> aggregate) {
    return this.aggregate(null, it -> true, identity(), aggregate);
  }
}