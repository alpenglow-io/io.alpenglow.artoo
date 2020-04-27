package artoo.query.many;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.$2.ConsInt;
import artoo.func.Func;
import artoo.query.One;
import artoo.query.Queryable;
import artoo.query.many.impl.Extremum;

import static java.lang.Integer.compare;
import static artoo.func.Func.identity;
import static artoo.type.Nullability.nonNullable;

interface Extremumable<T> extends Queryable<T> {
  @NotNull
  static <T, R> artoo.func.Func<? super T, Comparable<? super R>> comparing() {
    return mapped -> result -> compare(result.hashCode(), mapped.hashCode());
  }

  default <R> One<R> max(final Func<? super T, ? extends R> select) {
    return this.<R>extremum(1, comparing(), select);
  }

  default One<T> max() {
    return this.<T>extremum(1, comparing(), identity());
  }

  default <R> One<R> min(final Func<? super T, ? extends R> select) {
    return this.<R>extremum(-1, comparing(), select);
  }

  default One<T> min() {
    return this.<T>extremum(-1, comparing(), identity());
  }

  @NotNull
  @Contract("_, _, _ -> new")
  private <R> One<R> extremum(final int extremum, final Func<? super R, Comparable<? super R>> where, final Func<? super T, ? extends R> select) {
    //this.<R, R>aggregate(null, tautology(), select, (current, next) -> where.apply(next).compareTo(current) == extremum ? next : current);
    return new Extremum<>(this, ConsInt.nothing(), extremum, nonNullable(where, "where"), nonNullable(select, "select"))::iterator;
  }
}
