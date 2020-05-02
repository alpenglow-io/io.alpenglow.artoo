package io.artoo.query.many;



import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Extremum;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static java.lang.Integer.compare;
import static java.util.function.Function.identity;

interface Extremumable<T> extends Queryable<T> {
  @NotNull
  static <T, R> Function<? super T, Comparable<? super R>> comparing() {
    return mapped -> result -> compare(result.hashCode(), mapped.hashCode());
  }

  default <R> One<R> max(final Function<? super T, ? extends R> select) {
    return this.<R>extremum(1, comparing(), select);
  }

  default One<T> max() {
    return this.<T>extremum(1, comparing(), identity());
  }

  default <R> One<R> min(final Function<? super T, ? extends R> select) {
    return this.<R>extremum(-1, comparing(), select);
  }

  default One<T> min() {
    return this.<T>extremum(-1, comparing(), identity());
  }

  @NotNull
  @Contract("_, _, _ -> new")
  private <R> One<R> extremum(final int extremum, final Function<? super R, Comparable<? super R>> where, final Function<? super T, ? extends R> select) {
    //this.<R, R>aggregate(null, tautology(), select, (current, next) -> where.apply(next).compareTo(current) == extremum ? next : current);
    return new Extremum<>(this, (i, it) -> {}, extremum, nonNullable(where, "where"), nonNullable(select, "select"))::iterator;
  }
}
