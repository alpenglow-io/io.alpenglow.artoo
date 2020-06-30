package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.type.Eitherable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extrema<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A, R> One<A> aggregate(final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<T, A, R>(this, seed, nonNullable(where, "where"), nonNullable(select, "select"), nonNullable(aggregate, "aggregate"));
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
final class Aggregate<T, A, R> implements One<A>, Eitherable {
  private final Queryable<T> queryable;
  private final A seed;
  private final Pred.Uni<? super T> where;
  private final Func.Uni<? super T, ? extends R> select;
  private final Func.Bi<? super A, ? super R, ? extends A> aggregate;

  Aggregate(final Queryable<T> queryable, final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    assert queryable != null && where != null && select != null && aggregate != null;
    this.queryable = queryable;
    this.seed = seed;
    this.where = where;
    this.select = select;
    this.aggregate = aggregate;
  }

  @NotNull
  @Override
  public final Cursor<A> cursor() {
    var local = seed == null ? Cursor.<A>local() : Cursor.local(seed);

    final var cursor = queryable.cursor();
    while (cursor.hasNext()) {
      try {
        final var selected = cursor.next(it -> where.tryTest(it) ? select.tryApply(it) : null);

        local.next(local.hasNext() ? aggregate.tryApply(local.next(), selected) : (A) selected);

      } catch (Throwable throwable) {

        local.cause(throwable);
      }
    }

    return local;
  }
}
