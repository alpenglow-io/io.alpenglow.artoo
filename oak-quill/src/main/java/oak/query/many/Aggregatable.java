package oak.query.many;

import oak.cursor.Cursor;
import oak.func.$2.Func;
import oak.func.Cons;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.func.Cons.nothing;
import static oak.func.Func.identity;
import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extremumable<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A, R> One<A> aggregate(final A seed, final Pred<? super T> where, final oak.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(this, nothing(), seed, nonNullable(where, "where"), nonNullable(select, "select"), nonNullable(aggregate, "aggregate"));
  }

  default <A, R> One<A> aggregate(final A seed, final oak.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(seed, tautology(), select, aggregate);
  }

  default <A, R> One<A> aggregate(final oak.func.Func<? super T, ? extends R> select, final Func<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(null, tautology(), select, aggregate);
  }

  default <A> One<A> aggregate(final A seed, final Func<? super A, ? super T, ? extends A> aggregate) {
    return this.aggregate(seed, tautology(), identity(), aggregate);
  }

  default One<T> aggregate(final Func<? super T, ? super T, ? extends T> aggregate) {
    return this.aggregate(null, tautology(), identity(), aggregate);
  }
}

final class Aggregate<T, A, R> implements One<A> {
  private final Queryable<T> queryable;
  private final Cons<? super T> peek;
  private final A seed;
  private final Pred<? super T> where;
  private final oak.func.Func<? super T, ? extends R> select;
  private final Func<? super A, ? super R, ? extends A> aggregate;

  Aggregate(
    final Queryable<T> queryable,
    final Cons<? super T> peek,
    final A seed,
    final Pred<? super T> where,
    final oak.func.Func<? super T, ? extends R> select,
    final Func<? super A, ? super R, ? extends A> aggregate
  ) {
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
      peek.accept(it);
      if (where.test(it)) {
        final var apply = select.apply(it);
        aggregated = aggregate.apply(aggregated, apply);
      }
    }
    return Cursor.once(aggregated);
  }
}

