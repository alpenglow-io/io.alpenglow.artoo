package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;

public interface Aggregatable<T> extends Projectable<T> {
  default <A, R> Cursor<A> aggregate(final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return select(new Aggregate<>(seed, where, select, aggregate));
  }
}

final class Aggregate<T, A, R> implements Func.Uni<T, A> {
  private final Aggregated aggregated;
  private final Pred.Uni<? super T> where;
  private final Func.Uni<? super T, ? extends R> select;
  private final Func.Bi<? super A, ? super R, ? extends A> aggregate;

  Aggregate(final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    this.aggregated = new Aggregated(seed);
    this.where = where;
    this.select = select;
    this.aggregate = aggregate;
  }

  @SuppressWarnings("unchecked")
  @Override
  public A tryApply(final T it) throws Throwable {
    if (where.tryTest(it)) {
      final var selected = select.tryApply(it);

      aggregated.value = aggregated.value != null
        ? aggregate.tryApply(aggregated.value, selected)
        : (A) selected;
    }
    return aggregated.value;
  }

  private final class Aggregated {
    private A value;

    private Aggregated(final A value) {
      this.value = value;
    }
  }
}
