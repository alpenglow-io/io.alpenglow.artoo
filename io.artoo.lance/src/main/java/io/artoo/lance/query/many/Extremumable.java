package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.value.Numeric;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.artoo.lance.type.Nullability.nonNullable;
import static java.lang.Integer.compare;
import static java.util.function.Function.identity;

public interface Extremumable<T extends Record> extends Queryable<T> {
  @NotNull
  static <T, R> Function<? super T, Comparable<? super R>> comparing() {
    return mapped -> result -> compare(result.hashCode(), mapped.hashCode());
  }

  default <N extends Number, R extends Record & Numeric<R>> One<R> max(final Function<? super T, ? extends N> select) {
    return this.<R>extremum(1, comparing(), it -> Numeric.from(select.apply(it)));
  }

  default One<T> max() {
    return this.<T>extremum(1, comparing(), identity());
  }

  default <N extends Number, R extends Record & Numeric<R>> One<R> min(final Function<? super T, ? extends N> select) {
    return this.<R>extremum(-1, comparing(), it -> Numeric.from(select.apply(it)));
  }

  default One<T> min() {
    return this.<T>extremum(-1, comparing(), identity());
  }

  @NotNull
  @Contract("_, _, _ -> new")
  private <R extends Record> One<R> extremum(final int extremum, final Function<? super R, Comparable<? super R>> where, final Function<? super T, ? extends R> select) {
    //this.<R, R>aggregate(null, tautology(), select, (current, next) -> where.apply(next).compareTo(current) == extremum ? next : current);
    return new Extremum<>(this, (i, it) -> {}, extremum, nonNullable(where, "where"), nonNullable(select, "select"))::iterator;
  }
}

final class Extremum<T extends Record, R extends Record> implements Extremumable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final int extreme;
  private final Function<? super R, Comparable<? super R>> comparing;
  private final Function<? super T, ? extends R> select;

  @Contract(pure = true)
  Extremum(
    final Queryable<T> queryable,
    final BiConsumer<? super Integer, ? super T> peek,
    final int extreme,
    final Function<? super R, Comparable<? super R>> comparing,
    final Function<? super T, ? extends R> select
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
      peek.accept(index, it);
      if (it != null) {
        final var mapped = select.apply(it);
        if (result == null || mapped != null && comparing.apply(mapped).compareTo(result) == extreme)
          result = mapped;
      }
    }
    return Cursor.of(result);
  }
}
