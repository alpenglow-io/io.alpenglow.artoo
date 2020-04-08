package trydent.query.many.internal;

import trydent.cursor.Cursor;
import trydent.func.$2.Func;
import trydent.func.Cons;
import trydent.func.Pred;
import trydent.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Aggregate<T, A, R> implements Queryable<A> {
  private final Queryable<T> queryable;
  private final Cons<? super T> peek;
  private final A seed;
  private final Pred<? super T> where;
  private final trydent.func.Func<? super T, ? extends R> select;
  private final Func<? super A, ? super R, ? extends A> aggregate;

  @Contract(pure = true)
  public Aggregate(
    final Queryable<T> queryable,
    final Cons<? super T> peek,
    final A seed,
    final Pred<? super T> where,
    final trydent.func.Func<? super T, ? extends R> select,
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
    return Cursor.of(aggregated);
  }
}
