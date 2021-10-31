package io.artoo.lance.query.many;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.WhenType;
import io.artoo.lance.query.func.WhenWhere;

@SuppressWarnings("unchecked")
public interface Matchable<T> extends Queryable<T> {
  default Many<T> when(final Pred.MaybePredicate<? super T> pred, Cons.MaybeConsumer<? super T> cons) {
    return () -> cursor().map(new WhenWhere<>(pred, cons));
  }

  default <R> Many<T> when(final Class<R> type, Cons.MaybeConsumer<? super R> cons) {
    return when(type, unary(cons));
  }

  default <R> Many<T> when(final Class<R> type, Func.MaybeFunction<? super R, ? extends T> func) {
    return () -> cursor().map(new WhenType<>(type, func));
  }

  private <R> Func.MaybeFunction<R, T> unary(final Cons.MaybeConsumer<? super R> cons) {
    return it -> {
      cons.tryAccept(it);
      return (T) it;
    };
  }
}
