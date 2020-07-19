package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.operation.Aggregate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extrema<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A, R> One<A> aggregate(final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return One.done(() -> cursor().map(new Aggregate<>(seed, where, select, aggregate)));
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

