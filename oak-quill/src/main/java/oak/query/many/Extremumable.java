package oak.query.many;

import oak.cursor.Cursor;
import oak.func.$2.IntCons;
import oak.func.Func;
import oak.query.Queryable;
import oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.lang.Integer.compare;
import static oak.func.Func.identity;
import static oak.type.Nullability.nonNullable;

interface Extremumable<T> extends Queryable<T> {
  @NotNull
  static <T, R> oak.func.Func<? super T, Comparable<? super R>> comparing() {
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
    return new Extremum<>(this, IntCons.nothing(), extremum, nonNullable(where, "where"), nonNullable(select, "select"));
  }
}

final class Extremum<T, R> implements One<R> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final int extreme;
  private final Func<? super R, Comparable<? super R>> comparing;
  private final Func<? super T, ? extends R> select;

  Extremum(
    final Queryable<T> queryable,
    final IntCons<? super T> peek,
    final int extreme,
    final Func<? super R, Comparable<? super R>> comparing,
    final Func<? super T, ? extends R> select
  ) {
    this.queryable = queryable;
    this.peek = peek;
    this.extreme = extreme;
    this.comparing = comparing;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    R result = null;
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      var it = iterator.next();
      peek.acceptInt(index, it);
      if (it != null) {
        final var mapped = select.apply(it);
        if (result == null || mapped != null && comparing.apply(mapped).compareTo(result) == extreme)
          result = mapped;
      }
    }
    return Cursor.of(result);
  }
}
