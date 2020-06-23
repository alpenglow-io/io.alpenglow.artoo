package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extrema<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A, R> One<A> aggregate(final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    final var whr = nonNullable(where, "where");
    final var sel = nonNullable(select, "select");
    final var agr = nonNullable(aggregate, "aggregate");
    return new Aggregate<T, A, R>(this, it -> {}, seed, whr, sel, agr);
  }

  default <A, R> One<A> aggregate(final A seed, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(seed, it -> true, select, aggregate);
  }

  default <A, R> One<A> aggregate(final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(null, it -> true, select, aggregate);
  }

  default <A> One<A> aggregate(final A seed, final Func.Bi<? super A, ? super T, ? extends A> aggregate) {
    return this.aggregate(seed, it -> true, it -> it, aggregate);
  }

  default One<T> aggregate(final Func.Bi<? super T, ? super T, ? extends T> aggregate) {
    return this.aggregate(null, it -> true, it -> it, aggregate);
  }
}

@SuppressWarnings("unchecked")
final class Aggregate<T, A, R> implements One<A> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final A seed;
  private final Pred.Uni<? super T> where;
  private final Func.Uni<? super T, ? extends R> select;
  private final Func.Bi<? super A, ? super R, ? extends A> aggregate;

  Aggregate(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    assert queryable != null && peek != null && where != null && select != null && aggregate != null;
    this.queryable = queryable;
    this.peek = peek;
    this.seed = seed;
    this.where = where;
    this.select = select;
    this.aggregate = aggregate;
  }


  @NotNull
  @Override
  public final Iterator<A> iterator() {
    var aggregated = seed;
    for (final var it : queryable) {
      if (it != null) {
        peek.accept(it);
        if (where.test(it)) {
          final var selected = select.apply(it);
          if (selected != null) {
            aggregated = aggregated == null ? (A) selected : aggregate.apply(aggregated, selected);
          }
        }
      }
    }
    return Cursor.of(aggregated);
  }
}
